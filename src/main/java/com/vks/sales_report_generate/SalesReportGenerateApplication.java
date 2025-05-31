package com.vks.sales_report_generate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.vks.sales_report_generate.config.ExcelImportProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(ExcelImportProperties.class)
public class SalesReportGenerateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesReportGenerateApplication.class, args);
	}

}
