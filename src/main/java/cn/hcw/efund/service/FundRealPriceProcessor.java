package cn.hcw.efund.service;

import cn.hcw.efund.bean.Fund;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class FundRealPriceProcessor implements PageProcessor {

    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    public FundRealPriceProcessor(ElasticsearchRestTemplate elasticsearchRestTemplate){
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(500)
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");

    @Override
    public void process(Page page) {
        Map<String,String> data = handleGSJZData(page.getRawText());
        if (data.containsKey("fundCode")){
            Fund fund = elasticsearchRestTemplate.get(data.get("fundCode")+"_"+data.get("jzrq"), Fund.class);
            if (fund != null){
                fund.setEstimatedRate(new BigDecimal(data.get("gszzl")));
                fund.setEstimatedDate(data.get("gztime"));
                fund.setCurrentPrice(data.get("dwjz"));
                fund.setPriceDate(data.get("jzrq"));
                elasticsearchRestTemplate.save(fund);
            }

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    //处理估算净值
    private Map<String,String> handleGSJZData(String data) {

        Map<String, String> result = new HashMap<>();

        Pattern pattern = Pattern.compile("\\{.*?}");
        Matcher matcher = pattern.matcher(data);
        // 遍例所有匹配的序列
        while (matcher.find()) {
            String jsonData = matcher.group();
            if (StringUtils.isBlank(jsonData)){
                return result;
            }
            JSONObject jsonObject = JSON.parseObject(jsonData);
            String fundCode = jsonObject.getString("fundcode");
            String jzrq = jsonObject.getString("jzrq");//"净值日期
            String dwjz = jsonObject.getString("dwjz");//"单位净值
            String gsz = jsonObject.getString("gsz");//"估算净值
            String gszzl = jsonObject.getString("gszzl");//"估算增长率
            String gztime = jsonObject.getString("gztime");//"估算时间

            result.put("fundCode",fundCode);
            result.put("jzrq",jzrq);
            result.put("dwjz",dwjz);
            result.put("gsz",gsz);
            result.put("gszzl",gszzl);
            result.put("gztime",gztime);
        }
        return result;
    }
}
