package com.msb.oss.controller;


import com.msb.oss.model.dto.GenerateOssSignatureDTO;
import com.msb.oss.model.vo.OssSignatureVO;
import com.msb.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (OssFileRecord)表控制层
 *
 * @author makejava
 * @date 2022-03-30 10:48:38
 */
@Slf4j
@Api(tags = "阿里云上传文件相关")
@RestController
@RequestMapping
public class OssController {

    @Resource
    private OssService ossService;

    @ApiOperation("生成oss 调用签名")
    @PostMapping("oss/generateOssSignature")
    public OssSignatureVO generateOssSignature(@RequestBody GenerateOssSignatureDTO generateOssSignatureDTO) {
        return ossService.generateOssUploadSignature(generateOssSignatureDTO);
    }

}

