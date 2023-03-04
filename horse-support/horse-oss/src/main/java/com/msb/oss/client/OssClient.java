package com.msb.oss.client;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.msb.oss.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

@Slf4j
@Component
public class OssClient {

    @Resource
    private OssConfig ossConfig;

    public String uploadFile(InputStream inputStream, String filePath) {
        String upload = null;
        OSS client = null;
        try {
            client = new OSSClientBuilder()
                    .build(ossConfig.getEndpoint(), ossConfig.getAccessKey(), ossConfig.getAccessKeySecret());
            PutObjectResult putObjectResult = client.putObject(ossConfig.getBucket(), filePath, inputStream);
            log.info("putObjectResult {}", JSONObject.toJSONString(putObjectResult));
            return filePath;
        } catch (Exception e) {
            log.error("上传文件失败 ", e);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return upload;
    }
}
