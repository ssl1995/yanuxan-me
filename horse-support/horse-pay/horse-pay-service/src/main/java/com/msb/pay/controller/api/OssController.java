package com.msb.pay.controller.api;

import com.msb.pay.model.vo.OssSignatureVO;
import com.msb.pay.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "API-OSS文件")
@RestController
@RequestMapping("/oss")
public class OssController {

    @Resource
    private OssService ossService;

    @ApiOperation("oss调用签名")
    @GetMapping("/ossSignature")
    public OssSignatureVO ossSignature() {
        return ossService.ossSignature();
    }

    @ApiOperation(value = "上传文件", hidden = true)
    @PostMapping("/upload")
    public Boolean upload(String objectPath, String filePath) {
        return ossService.upload(objectPath, filePath);
    }

    @ApiOperation(value = "下载文件", hidden = true)
    @PostMapping("/download")
    public Boolean download(String objectPath, String filePath) {
        return ossService.download(objectPath, filePath);
    }

}
