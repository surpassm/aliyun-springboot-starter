package com.github.surpassm.aliyun.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mc
 * Create date 2020/12/14 16:30
 * Version 1.0
 * Description
 */
public class FileUtils {


    public static String getFileName(MultipartFile file) {
        return file.getOriginalFilename().replace("." + getFileType(file), "");
    }

    public static String getFileType(MultipartFile file) {
        String[] s = file.getOriginalFilename().split("\\.");
        List list = new ArrayList();
        for (String s1 : s) {
            list.add(s1);
        }
        if (list.size() > 1) {
            return list.get(list.size() - 1).toString();
        }
        return null;
    }
}
