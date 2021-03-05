package com.github.surpassm.aliyun.pojo;

import lombok.*;

/**
 * @author mc
 * Create date 2020/12/10 16:02
 * Version 1.0
 * Description
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcsConfig {

    /**
     * 地域ID
     */
    private String mpsRegionId="";
    /**
     * RAM账号的AccessKey ID
     */
    private String accessKeyId="";
    /**
     * RAM账号Access Key Secret
     */
    private String accessKeySecret="";
}
