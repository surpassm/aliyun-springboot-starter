package com.github.surpassm.aliyun.api.oss;

import com.aliyun.oss.model.AccessControlList;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.CannedAccessControlList;
import com.github.surpassm.aliyun.pojo.OssFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author mc
 * Create date 2020/12/7 10:23
 * Version 1.0
 * Description
 */
public interface OssService {
    /**
     * 判断存储空间是否存在
     *
     * @param bucketName 你的桶名
     * @return boolean
     */
    Boolean doesBucketExist(String bucketName);

    /**
     * 创建存储空间
     *
     * @param bucketName 你的桶名
     * @return Bucket
     */
    void createStorageSpace(String bucketName);

    /**
     * 删除存储空间
     *
     * @param bucketName 你的桶名
     * @return boolean
     */
    Boolean deleteStorageSpace(String bucketName);

    /**
     * 获取存储空间的地域
     *
     * @param bucketName 你的桶名
     * @return boolean
     */
    String getBucketLocation(String bucketName);

    /**
     * 获取存储空间的信息
     *
     * @param bucketName 你的桶名
     * @return Info
     * 存储空间的信息包括地域（Region或Location）、创建日期（CreationDate）、拥有者（Owner）、权限（Grants）
     */
    BucketInfo getBucketInfo(String bucketName);

    /**
     * 管理存储空间访问权限
     * 存储空间的访问权限（ACL）有以下三类:
     * 私有 CannedAccessControlList.Private
     * 公共读 CannedAccessControlList.PublicRead
     * 公共读写 CannedAccessControlList.PublicReadWrite
     *
     * @param bucketName              你的桶名
     * @param cannedAccessControlList 你的桶名
     */
    void setBucketAcl(String bucketName, CannedAccessControlList cannedAccessControlList);

    /**
     * 获取存储空间访问权限
     * @param bucketName 你的桶名
     * @return
     */
    AccessControlList getBucketAcl(String bucketName);

    /**
     * 普通上传
     *
     * @param files  Breakpoint continuation
     */
    List<OssFile> uploadFiles(MultipartFile[] files, String yourBucketName);
    /**
     * 普通上传
     *
     * @param file  Breakpoint continuation
     */
    OssFile uploadFile(File file, String yourBucketName);

    OssFile uploadMultipartFile(MultipartFile file, String yourBucketName);


}
