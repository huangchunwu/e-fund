package cn.hcw.efund.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(indexName = "fund_info")
public class Fund {

    @Field(type = FieldType.Keyword)
    private String name;
    @Id
    private String code;
    private String currentPrice;
    private String estimatedRate;
    private String estimatedDate;
    private String fee;
    private String status;
    private String type;
    private String risk;
    private String amount;
    private String manager;
    private String startTime;
    private String company;
    private String oneDay;
    private String oneWeek;
    private String oneMonth;
    private String threeMonth;
    private String sixMonth;
    private String oneYear;
    private String twoYear;
    private String threeYear;
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


}
