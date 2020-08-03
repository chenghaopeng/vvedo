package cn.chper.vvdo.server.util;

import cn.chper.vvdo.server.config.OssConfig;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import org.springframework.web.multipart.MultipartFile;

public class AliOssUtil {

    private static volatile OSSClient ossClient = null;

    public static String upload(MultipartFile file, String path, OssConfig ossConfig) {
        init(ossConfig);
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = System.currentTimeMillis() + "-" + Utils.getRandomString() + suffix;
        String filePath = path + "/" + fileName;
        try {
            ossClient.putObject(ossConfig.getBucketName(), filePath, file.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ossConfig.getUrl() + filePath;
    }

    private static void init(OssConfig ossConfig) {
        if (ossClient == null) {
            synchronized (AliOssUtil.class) {
                if (ossClient == null) {
                    ossClient = new OSSClient(ossConfig.getEndpoint(), new DefaultCredentialProvider(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret()), new ClientConfiguration());
                }
            }
        }
    }

}
