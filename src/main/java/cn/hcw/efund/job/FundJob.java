package cn.hcw.efund.job;


import cn.hcw.efund.bean.Fund;
import cn.hcw.efund.service.FundProcessor;
import cn.hcw.efund.service.FundRealPriceProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.util.Arrays;


@Service
public class FundJob implements InitializingBean {

    private String url = "http://fund.eastmoney.com/";
    private String gzUrl ="https://fundgz.1234567.com.cn/js/";
    @Value("${fund.monitor.list}")
    private String fundList;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 每天盘后更新一下
     */
   @Scheduled(cron = "0 0 1,19/1 * * ?")
    public void run(){
        String[] split = fundList.split(",");
        Arrays.asList(split).stream().forEach(fundCode->{
            Spider.create(new FundProcessor(elasticsearchRestTemplate,new Fund())).addUrl(url+fundCode+".html").thread(1).run();
            Spider.create(new FundRealPriceProcessor(elasticsearchRestTemplate)).addUrl(gzUrl+fundCode+".js").thread(1).run();
        });
    }

    @Scheduled(cron = "0 0/5 9-15 * * ?")
    public void realTimePriceScheduled(){
        String[] split = fundList.split(",");
        Arrays.asList(split).stream().forEach(fundCode->{
            Spider.create(new FundRealPriceProcessor(elasticsearchRestTemplate)).addUrl(gzUrl+fundCode+".js").thread(1).run();
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        run();
        realTimePriceScheduled();
    }
}
