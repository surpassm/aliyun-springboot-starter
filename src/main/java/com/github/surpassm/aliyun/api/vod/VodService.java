package com.github.surpassm.aliyun.api.vod;

import com.aliyuncs.vod.model.v20170321.*;
import com.github.surpassm.aliyun.pojo.VodFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author mc
 * Create date 2020/12/14 14:48
 * Version 1.0
 * Description
 */
public interface VodService {


    List<VodFile> uploadVideo(MultipartFile[] files, String yourBucketName);

    VodFile uploadVideo(MultipartFile file, String yourBucketName);

    GetVideoPlayAuthResponse getPlayAuth(String videoId);
    /**
     * 支持传入多个视频ID，多个用逗号分隔
     */
    DeleteVideoResponse deleteVideo(String videoIds);

    GetMezzanineInfoResponse downFile(String videoIds);

    /**
     * 提交媒体处理作业
     */
    SubmitTranscodeJobsResponse submitTranscodeJobs(String videoIds,String templateGroupId);

    /**
     * 查询转码模板组列表
     */

    ListTranscodeTemplateGroupResponse listTranscodeTemplateGroup();

}
