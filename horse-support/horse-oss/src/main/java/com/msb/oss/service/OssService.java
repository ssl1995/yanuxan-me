package com.msb.oss.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.msb.oss.config.OssConfig;
import com.msb.oss.model.dto.GenerateOssSignatureDTO;
import com.msb.oss.model.vo.OssSignatureVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OssService {

    @Resource
    private OssConfig ossConfig;

    @Resource
    private OSS ossClient;

    private String generateFilePathByConfig(String configId, Map<String, String> paramMap) {
        //todo 根据config 生成文件存储目录
        return configId;
    }

    private String callbackBody(String serviceName) {
        JSONObject jasonCallback = new JSONObject();
        jasonCallback.put("callbackUrl", ossConfig.getCallback());
        jasonCallback.put("callbackBody",
                "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}&serviceName=" + serviceName);
        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
        return BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
    }

    private boolean existFile(String filePath) {
        return ossClient.doesObjectExist(ossConfig.getBucket(), filePath);
    }

    public OssSignatureVO generateOssUploadSignature(GenerateOssSignatureDTO generateOssSignatureDTO) {
        String filePath = generateOssSignatureDTO.getServiceName().concat("/")
                .concat(generateFilePathByConfig(generateOssSignatureDTO.getConfigId(), generateOssSignatureDTO.getParamMap()));
        long expireEndTime = System.currentTimeMillis() + ossConfig.getExpire();
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, filePath);
        String postPolicy = ossClient.generatePostPolicy(new Date(expireEndTime), policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String callback = callbackBody(generateOssSignatureDTO.getServiceName());
        return OssSignatureVO.builder()
                .accessId(ossConfig.getAccessKey())
                .policy(BinaryUtil.toBase64String(binaryData))
                .signature(ossClient.calculatePostSignature(postPolicy))
                .dir(filePath)
                .host(ossConfig.getHost())
                .expire(expireEndTime / 1000)
                .callback(callback)
                .build();
    }

}
