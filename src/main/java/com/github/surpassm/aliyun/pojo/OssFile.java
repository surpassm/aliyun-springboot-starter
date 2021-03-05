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
public class OssFile {

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
     * 指定要上传到 COS 上对象键
     */
    private String cosKey;
    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 文件路径
     */
    private String objectName;

    private MultipartFile multipartFile;
}
