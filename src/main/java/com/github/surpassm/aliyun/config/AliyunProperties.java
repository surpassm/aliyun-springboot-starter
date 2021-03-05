package com.github.surpassm.aliyun.config;

import com.github.surpassm.aliyun.pojo.AcsConfig;
import com.github.surpassm.aliyun.pojo.OssConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mc
 * Create date 2020/12/7 9:54
 * Version 1.0
 * Description
 */
@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "aliyun")
public class AliyunProperties {

    /**
     * 是否开启Aliyun
     **/
    private Boolean enabled = true;

    private OssConfig ossConfig = new OssConfig();

    private AcsConfig acsConfig = new AcsConfig();

    private OssConfig vodConfig = new OssConfig();


}

