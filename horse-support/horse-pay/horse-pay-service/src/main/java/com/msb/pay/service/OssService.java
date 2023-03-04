package com.msb.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.msb.pay.config.OssConfig;
import com.msb.pay.model.vo.OssSignatureVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service("ossService")
public class OssService {

    @Resource
    private OssConfig ossConfig;
    @Resource
    private OSS ossClient;

    private String callbackBody(String serviceName) {
        JSONObject jasonCallback = new JSONObject();
        jasonCallback.put("callbackUrl", "");
        jasonCallback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}&serviceName=" + serviceName);
        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
        return BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
    }

    public OssSignatureVO ossSignature() {
        long expireEndTime = System.currentTimeMillis() + 10000000;
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, ossConfig.getDir());
        String postPolicy = ossClient.generatePostPolicy(new Date(expireEndTime), policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String callback = callbackBody(ossConfig.getDir());
        return OssSignatureVO.builder()
                .accessId(ossConfig.getAccessKey())
                .policy(BinaryUtil.toBase64String(binaryData))
                .signature(ossClient.calculatePostSignature(postPolicy))
                .dir(ossConfig.getDir())
                .host(ossConfig.getHost())
                .expire(expireEndTime / 1000)
                .callback(callback)
                .build();
    }

    /**
     * 上传文件
     *
     * @param objectPath：OSS文件路径
     * @param filePath：上传的本地文件路径
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/7/8
     */
    public Boolean upload(String objectPath, String filePath) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucket(), objectPath, new File(filePath));
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            log.info("上传文件RequestId：{}", putObjectResult.getRequestId());
            return true;
        } catch (Exception e) {
            log.info("上传文件失败", e);
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param objectPath：OSS文件路径
     * @param filePath：下载到本地文件路径
     * @return java.lang.Boolean
     * @author peng.xy
     * @date 2022/7/8
     */
    public Boolean download(String objectPath, String filePath) {
        try {
            ObjectMetadata objectMetadata = ossClient.getObject(new GetObjectRequest(ossConfig.getBucket(), objectPath), new File(filePath));
            log.info("下载文件RequestId：{}", objectMetadata.getRequestId());
            return true;
        } catch (Exception e) {
            log.info("下载文件失败", e);
            return false;
        }
    }

}
