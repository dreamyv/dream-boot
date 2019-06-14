package com.dream.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {

    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    //分割符
    public static final String DASH = "/";

    /**
     * 上传监控图片
     */
    public  String uploadMonImage(String name, MultipartFile file) throws Exception {
        String fileName = "JMY"+ "/"+ name;
        File targetFile = new File(fileName);
        if (!targetFile.getParentFile().exists()) {
           targetFile.getParentFile().mkdirs();
        }
        file.transferTo(targetFile);
        return targetFile.getAbsolutePath();
    }

    /**
     * 获取当前时间文件夹名称
     * @return
     */
    public String getDateName() {
        return new SimpleDateFormat("YYYYMMdd").format(new Date());
    }

    /**
     * 图片上传
     */
    public  String uploadMonImageOld(String vin, String name, String file) throws Exception {
        String fileName = "";
        byte[] bytes = file.getBytes("ISO-8859-1");
        fileName = "JMY"+"/"+vin + "_" + name;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "JMY" + fileName;
    }

    public  String uploadMonFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        // 获取文件名
        String fileName = "JMY" + "/" + file.getOriginalFilename();
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(   fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
