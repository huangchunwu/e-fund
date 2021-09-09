package cn.hcw.efund.job;


import cn.hcw.efund.bean.Fund;
import cn.hcw.efund.service.FundProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;


@Service
public class FundJob {

    private String url = "http://fund.eastmoney.com/004752.html";

    @Scheduled(cron = "0/5 * * * * *")
    public void run(){
        Spider.create(new FundProcessor(new Fund())).addUrl(url).thread(1).run();
    }

}
