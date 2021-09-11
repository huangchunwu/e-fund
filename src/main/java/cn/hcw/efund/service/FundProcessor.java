package cn.hcw.efund.service;

import cn.hcw.efund.bean.Fund;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;

@Slf4j
public class FundProcessor implements PageProcessor {


    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    private Fund fund;

    public FundProcessor(ElasticsearchRestTemplate elasticsearchRestTemplate,Fund fund){
        this.fund = fund;
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
        try {
            List<Selectable> foundInfos =  page.getHtml().xpath("//div[@class='infoOfFund']/table/tbody/tr/td").nodes();
            String type = foundInfos.get(0).xpath("//a/text()").toString();
            String risk = foundInfos.get(0).xpath("//td/text()").toString();
            risk = risk.replace("基金类型：", "").replaceAll("\\|", "").replaceAll("\\s*", "");
            String manager = foundInfos.get(2).xpath("//a/text()").toString();
            String startTime = foundInfos.get(3).xpath("//td/text()").toString().replace("：", "");

            List<Selectable> increaseInfos =  page.getHtml().xpath("//li[@class='increaseAmount']/table[@class='ui-table-hover']/tbody/tr").nodes().get(1).xpath("//td").nodes();
            String oneWeek = increaseInfos.get(1).xpath("//div/text()").toString().replace("%", "");
            String oneMonth = increaseInfos.get(2).xpath("//div/text()").toString().replace("%", "");
            String threeMonth = increaseInfos.get(3).xpath("//div/text()").toString().replace("%", "");
            String sixMonth = increaseInfos.get(4).xpath("//div/text()").toString().replace("%", "");
            String oneYear = increaseInfos.get(6).xpath("//div/text()").toString().replace("%", "");
            String twoYear = increaseInfos.get(7).xpath("//div/text()").toString().replace("%", "");
            String threeYear = increaseInfos.get(8).xpath("//div/text()").toString().replace("%", "");

            List<Selectable> nodes = page.getHtml().xpath("//div[@class='fundDetail-tit']/div").nodes();
            String code = nodes.get(0).xpath("//span[@class='ui-num']/text()").toString();
            String name = nodes.get(0).toString().substring(nodes.get(0).toString().indexOf("\n"),nodes.get(0).toString().indexOf("<span>")).replaceAll("\n","");


            List<Selectable> nodes1 = page.getHtml().xpath("//dl[@class='dataItem01']").nodes();
            String s = nodes1.get(0).toString();

            fund.setCode(code);
            fund.setName(name);
            fund.setType(type);
            fund.setRisk(risk);
            fund.setManager(manager);
            fund.setStartTime(startTime);

            fund.setOneWeek(oneWeek);
            fund.setOneMonth(oneMonth);
            fund.setThreeMonth(threeMonth);
            fund.setSixMonth(sixMonth);
            fund.setOneYear(oneYear);
            fund.setTwoYear(twoYear);
            fund.setThreeYear(threeYear);
            log.info(JSON.toJSONString(fund));

            elasticsearchRestTemplate.save(fund);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}
