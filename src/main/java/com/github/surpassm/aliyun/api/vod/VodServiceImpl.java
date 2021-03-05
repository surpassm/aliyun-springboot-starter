package com.github.surpassm.aliyun.api.vod;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyRequest;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.github.surpassm.aliyun.config.AliyunConfig;
import com.github.surpassm.aliyun.config.AliyunProperties;
import com.github.surpassm.aliyun.config.AliyunThreadConfig;
import com.github.surpassm.aliyun.exception.CustomException;
import com.github.surpassm.aliyun.pojo.OssFile;
import com.github.surpassm.aliyun.pojo.VodFile;
import com.github.surpassm.aliyun.util.FileUtils;
import com.github.surpassm.aliyun.util.OrderUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mc
 * Create date 2020/12/14 14:48
 * Version 1.0
 * Description
 */
@Slf4j
@Service
public class VodServiceImpl implements VodService {
    @Resource
    private AliyunThreadConfig aliyunThreadConfig;
    @Resource
    private AliyunProperties aliyunProperties;

    @Override
    public List<VodFile> uploadVideo(MultipartFile[] files, String yourBucketName) {
        List<VodFile> vodFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String callbackId = OrderUtil.getOrderNo();
            String oldFileName = file.getOriginalFilename();
            String fileName = FileUtils.getFileName(file);
            String fileSuffix = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = OrderUtil.getOrderNo() + fileSuffix;
//            Objects.requireNonNull(aliyunThreadConfig.getAsyncExecutor()).execute(() -> {
            try {
                UploadStreamResponse response = uploadStream(aliyunProperties.getVodConfig().getAccessKeyId(),
                        aliyunProperties.getVodConfig().getAccessKeySecret(),
                        fileName,
                        newFileName,
                        file.getInputStream());
                if (response.isSuccess()) {
                    String videoId = response.getVideoId();
                    vodFiles.add(VodFile.builder()
                            .fileOldName(fileName)
                            .fileSuffix(fileSuffix)
                            .fileNewName(newFileName)
                            .bucketName(yourBucketName)
                            .multipartFile(file)
                            .callbackId(callbackId)
                            .videoId(videoId)
                            .build());
                } else {
                    log.error("ErrorCode=" + response.getCode());
                    log.error("ErrorMessage=" + response.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            });

        }
        return vodFiles;
    }

    @Override
    public VodFile uploadVideo(MultipartFile file, String yourBucketName) {
        String callbackId = OrderUtil.getOrderNo();
        String oldFileName = file.getOriginalFilename();
        String fileName = FileUtils.getFileName(file);
        String fileSuffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = OrderUtil.getOrderNo() + fileSuffix;
//            Objects.requireNonNull(aliyunThreadConfig.getAsyncExecutor()).execute(() -> {
        try {
            UploadStreamResponse response = uploadStream(aliyunProperties.getVodConfig().getAccessKeyId(),
                    aliyunProperties.getVodConfig().getAccessKeySecret(),
                    fileName,
                    newFileName,
                    file.getInputStream());
            if (response.isSuccess()) {
                String videoId = response.getVideoId();
                return VodFile.builder()
                        .fileOldName(fileName)
                        .fileSuffix(fileSuffix)
                        .fileNewName(newFileName)
                        .bucketName(yourBucketName)
                        .multipartFile(file)
                        .callbackId(callbackId)
                        .videoId(videoId)
                        .build();
            } else {
                log.error("ErrorCode=" + response.getCode());
                log.error("ErrorMessage=" + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            });
        throw new CustomException(400, "");
    }

    @Override
    public GetVideoPlayAuthResponse getPlayAuth(String videoId) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunProperties.getVodConfig().getAccessKeyId(),
                aliyunProperties.getVodConfig().getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setRegionId("cn-hangzhou");
        request.setVideoId(videoId);
        try {
            return client.getAcsResponse(request);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
        return null;
    }

    @Override
    public DeleteVideoResponse deleteVideo(String videoIds) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunProperties.getVodConfig().getAccessKeyId(),
                aliyunProperties.getVodConfig().getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoIds);
        try {
            return client.getAcsResponse(request);
        } catch (Exception e) {
            log.error("ErrorMessage = " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public GetMezzanineInfoResponse downFile(String videoIds) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunProperties.getVodConfig().getAccessKeyId(),
                aliyunProperties.getVodConfig().getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        try {
            return getMezzanineInfo(client, videoIds);
        } catch (Exception e) {
            log.error("视频点播下载：ErrorMessage = " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public SubmitTranscodeJobsResponse submitTranscodeJobs(String videoIds,String templateGroupId) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunProperties.getVodConfig().getAccessKeyId(),
                aliyunProperties.getVodConfig().getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        SubmitTranscodeJobsRequest request = new SubmitTranscodeJobsRequest();
        //需要转码的视频ID
        request.setVideoId(videoIds);
        //转码模板ID
        request.setTemplateGroupId(templateGroupId);
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            return null;
        }
    }

    @Override
    public ListTranscodeTemplateGroupResponse listTranscodeTemplateGroup() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunProperties.getVodConfig().getAccessKeyId(),
                aliyunProperties.getVodConfig().getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        try {
            return client.getAcsResponse(new ListTranscodeTemplateGroupRequest());
        } catch (Exception e) {
            throw new CustomException("查询转码模板组列表ErrorMessage = " + e.getLocalizedMessage());
        }
    }

    /**
     * 流式上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param inputStream
     */
    private static UploadStreamResponse uploadStream(String accessKeyId, String accessKeySecret, String title,
                                                     String fileName, InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setShowWaterMark(true);
        /* 自定义消息回调设置，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData */

//        request.setUserData("{\"Extend\":{\"callbackId\":\""+callbackId+"\"},\"MessageCallback\":{\"CallbackURL\":\"http://test.test.com\"}}");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 工作流ID(可选) */
        //request.setWorkflowId("d4430d07361f0*be1339577859b0177b");
        /* 存储区域(可选) */
//        request.setStorageLocation("outin-7b4feb363dd711eb869b00163e00b174.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
        // request.setPrintProgress(true);
        /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
        // request.setProgressListener(new PutObjectProgressListener());
        /* 设置应用ID*/
        //request.setAppId("app-1000000");
        /* 点播服务接入点 */
        //request.setApiRegionId("cn-shanghai");
        /* ECS部署区域*/
        // request.setEcsRegionId("cn-shanghai");
        UploadVideoImpl uploader = new UploadVideoImpl();
        return uploader.uploadStream(request);
    }


    /**
     * 获取源文件信息
     *
     * @param client 发送请求客户端
     * @return GetMezzanineInfoResponse 获取源文件信息响应数据
     * @throws Exception
     */
    public static GetMezzanineInfoResponse getMezzanineInfo(DefaultAcsClient client, String videoIds) throws Exception {
        GetMezzanineInfoRequest request = new GetMezzanineInfoRequest();
        request.setVideoId(videoIds);
        //源片下载地址过期时间
        request.setAuthTimeout(3600 * 60L);
        return client.getAcsResponse(request);
    }

    /**
     * 生成加密需要的秘钥，response中包含密文秘钥和明文秘钥，用户只需要将密文秘钥传递给点播即可
     * 注意：KeySpec 必须传递AES_128，且不能设置NumberOfBytes
     *
     * @param client     KMS-SDK客户端
     * @param serviceKey 点播提供生成秘钥的service key，在用户的秘钥管理服务中可看到描述为vod的加密key
     * @return
     * @throws ClientException
     */
    public static GenerateDataKeyResponse generateDataKey(DefaultAcsClient client, String serviceKey) throws ClientException {
        GenerateDataKeyRequest request = new GenerateDataKeyRequest();
        request.setKeyId(serviceKey);
        request.setKeySpec("AES_128");
        return client.getAcsResponse(request);
    }
}
