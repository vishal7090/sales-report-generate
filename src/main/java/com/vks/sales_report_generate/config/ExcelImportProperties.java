package com.vks.sales_report_generate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "excel")
public class ExcelImportProperties {
    private String folderPath;
} 