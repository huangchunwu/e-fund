package cn.hcw.efund.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Document(indexName = "fund_info")
public class Fund {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    private String code;
    private String currentPrice;
    private String priceDate;
    private BigDecimal estimatedRate;
    private String estimatedDate;
    private String fee;
    private String status;
    private String type;
    private String risk;
    private String amount;
    private String manager;
    private String startTime;
    private String company;
    private BigDecimal oneDay;
    private BigDecimal oneWeek;
    private BigDecimal oneMonth;
    private BigDecimal threeMonth;
    private BigDecimal sixMonth;
    private BigDecimal oneYear;
    private BigDecimal twoYear;
    private BigDecimal threeYear;
    @Field(type = FieldType.Date,
            format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime addTime;

    @Field(type = FieldType.Date,
            format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime = LocalDateTime.now();


    @Field(name = "@timestamp")
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public void generatedId(){
        this.id = this.code+"_"+this.priceDate;
    }
}
