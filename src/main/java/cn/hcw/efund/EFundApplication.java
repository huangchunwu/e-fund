package cn.hcw.efund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class EFundApplication {

    public static void main(String[] args) {
        SpringApplication.run(EFundApplication.class, args);
    }

}
