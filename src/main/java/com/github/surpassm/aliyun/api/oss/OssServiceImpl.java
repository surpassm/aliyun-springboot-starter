package com.github.surpassm.aliyun.api.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.surpassm.aliyun.config.AliyunConfig;
import com.github.surpassm.aliyun.config.AliyunProperties;
import com.github.surpassm.aliyun.config.AliyunThreadConfig;
import com.github.surpassm.aliyun.exception.CustomException;
import com.github.surpassm.aliyun.pojo.OssFile;
import com.github.surpassm.aliyun.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.beans.Customizer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @author mc
 * Create date 2020/12/7 10:25
 * Version 1.0
 * Description
 */
@Service
public class OssServiceImpl implements OssService {

    @Resource
    private OSS ossClient;
    @Resource
    private AliyunConfig aliyunConfig;
    @Resource
    private AliyunThreadConfig aliyunThreadConfig;
    @Resource
    private AliyunProperties aliyunProperties;



    @Override
    public Boolean doesBucketExist(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    @Override
    public void createStorageSpace(String bucketName) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 创建存储空间。
        Objects.requireNonNull(aliyunThreadConfig.getAsyncExecutor()).execute(() -> {
            try {
                aliyunConfig.newOssClient().createBucket(createBucketRequest);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(401,"创建存储空间异常");
            } finally {
                aliyunConfig.newOssClient().shutdown();
            }
        });
    }

    @Override
    public Boolean deleteStorageSpace(String bucketName) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 删除存储空间。
        ossClient.deleteBucket(createBucketRequest);
        return true;
    }

    @Override
    public String getBucketLocation(String bucketName) {
        return ossClient.getBucketLocation(bucketName);
    }

    @Override
    public BucketInfo getBucketInfo(String bucketName) {
        return aliyunConfig.newOssClient().getBucketInfo(bucketName);
    }

    @Override
    public void setBucketAcl(String bucketName, CannedAccessControlList cannedAccessControlList) {
        ossClient.setBucketAcl(bucketName, cannedAccessControlList);
    }

    @Override
    public AccessControlList getBucketAcl(String bucketName) {
        return ossClient.getBucketAcl(bucketName);
    }


    @Override
    public List<OssFile> uploadFiles(MultipartFile[] files,String yourBucketName) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        String endpoint = aliyunProperties.getOssConfig().getEndpoint();
        String domainApp = endpoint.substring(0,8)+yourBucketName+"."+endpoint.substring(8 ,endpoint.length())+"/";

        //设置存储类型与访问权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setObjectAcl(CannedAccessControlList.PublicRead);

        List<OssFile> ossFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = OrderUtil.getOrderNo() + eName;
            String key = year + "/" + month + "/" + day + "/" + newFileName;
            // 创建PutObjectRequest对象。 异步上传至阿里云对象存储
            Objects.requireNonNull(aliyunThreadConfig.getAsyncExecutor()).execute(() -> {
                try {
                    PutObjectRequest putObjectRequest = new PutObjectRequest(yourBucketName, key, file.getInputStream());
                    putObjectRequest.setMetadata(metadata);
                    aliyunConfig.newOssClient().putObject(putObjectRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    aliyunConfig.newOssClient().shutdown();
                }
            });

            ossFiles.add(OssFile.builder()
                    .fileOldName(oldFileName)
                    .fileSuffix(eName)
                    .fileNewName(newFileName)
                    .cosKey(key)
                    .bucketName(yourBucketName)
                    .objectName(key)
                    .url(domainApp + key)
                    .multipartFile(file)
                    .build());

        }
        return ossFiles;
    }

    @Override
    public OssFile uploadFile(File file, String yourBucketName) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        String endpoint = aliyunProperties.getOssConfig().getEndpoint();
        String domainApp = endpoint.substring(0,8)+yourBucketName+"."+endpoint.substring(8 ,endpoint.length())+"/";

        //设置存储类型与访问权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setObjectAcl(CannedAccessControlList.PublicRead);

        String oldFileName = file.getName();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = OrderUtil.getOrderNo() + eName;
        String key = year + "/" + month + "/" + day + "/" + newFileName;
        // 创建PutObjectRequest对象。 异步上传至阿里云对象存储
        Objects.requireNonNull(aliyunThreadConfig.getAsyncExecutor()).execute(() -> {
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(yourBucketName, key, file);
                putObjectRequest.setMetadata(metadata);
                aliyunConfig.newOssClient().putObject(putObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                aliyunConfig.newOssClient().shutdown();
            }
        });

        return OssFile.builder()
                .fileOldName(oldFileName)
                .fileSuffix(eName)
                .fileNewName(newFileName)
                .cosKey(key)
                .bucketName(yourBucketName)
                .objectName(key)
                .url(domainApp + key)
                .build();
    }

    @Override
    public OssFile uploadMultipartFile(MultipartFile file, String yourBucketName) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        String endpoint = aliyunProperties.getOssConfig().getEndpoint();
        String domainApp = endpoint.substring(0,8)+yourBucketName+"."+endpoint.substring(8 ,endpoint.length())+"/";

        //设置存储类型与访问权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setObjectAcl(CannedAccessControlList.PublicRead);

        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = OrderUtil.getOrderNo() + eName;
        String key = year + "/" + month + "/" + day + "/" + newFileName;
        // 创建PutObjectRequest对象。 异步上传至阿里云对象存储
        Objects.requireNonNull(aliyunThreadConfig.getAsyncExecutor()).execute(() -> {
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(yourBucketName, key, file.getInputStream());
                putObjectRequest.setMetadata(metadata);
                aliyunConfig.newOssClient().putObject(putObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                aliyunConfig.newOssClient().shutdown();
            }
        });

        return OssFile.builder()
                .fileOldName(oldFileName)
                .fileSuffix(eName)
                .fileNewName(newFileName)
                .cosKey(key)
                .bucketName(yourBucketName)
                .objectName(key)
                .url(domainApp + key)
                .build();
    }

}
