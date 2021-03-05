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
public class OssConfig {

    /**
     * 节点
     */
    private String endpoint="";
    /**
     * 秘钥
     */
    private String accessKeyId="";
    /**
     * 密匙
     */
    private String accessKeySecret="";
}
