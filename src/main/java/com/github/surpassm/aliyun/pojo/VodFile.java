package com.github.surpassm.aliyun.pojo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mc
 * Create date 2020/12/10 15:54
 * Version 1.0
 * Description
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VodFile {

    /**
     * 文件名称
     */
    private String fileOldName;
    /**
     * 文件名称
     */
    private String fileNewName;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件路径
     */
    private String url;
    /**
     * vod视频ID
     */
    private String videoId;
    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 文件路径
     */
    private String objectName;

    private String callbackId;

    private MultipartFile multipartFile;

    private String playAuth;

}
