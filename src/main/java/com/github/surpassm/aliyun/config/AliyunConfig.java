package com.github.surpassm.aliyun.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author mc
 * Create date 2020/12/7 9:53
 * Version 1.0
 * Description
 */
@Configuration
public class AliyunConfig {

    @Resource
    private AliyunProperties aliyunProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "aliyun.enabled", matchIfMissing = true)
    public OSS ossClient() {
        return new OSSClientBuilder().build(aliyunProperties.getOssConfig().getEndpoint(), aliyunProperties.getOssConfig().getAccessKeyId(), aliyunProperties.getOssConfig().getAccessKeySecret());
    }

    public OSS newOssClient(){
        return new OSSClientBuilder().build(aliyunProperties.getOssConfig().getEndpoint(), aliyunProperties.getOssConfig().getAccessKeyId(), aliyunProperties.getOssConfig().getAccessKeySecret());
    }
}
