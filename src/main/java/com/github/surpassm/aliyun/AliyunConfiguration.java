package com.github.surpassm.aliyun;

import com.github.surpassm.aliyun.api.oss.OssServiceImpl;
import com.github.surpassm.aliyun.config.AliyunProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mc
 * Create date 2020/12/10 15:10
 * Version 1.0
 * Description
 */

@Configuration
@ComponentScan({"com.github.surpassm.aliyun"})
@EnableConfigurationProperties({AliyunProperties.class})
@ConditionalOnProperty(name = "aliyun.enabled", matchIfMissing = true)
public class AliyunConfiguration {
}
