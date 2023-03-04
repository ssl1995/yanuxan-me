package com.msb.mall.comment.controller.admin;


import com.msb.mall.comment.model.dto.admin.MerchantCommentDTO;
import com.msb.mall.comment.model.vo.admin.MerchantCommentVO;
import com.msb.mall.comment.service.MerchantCommentService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 商家评论(MerchantComment)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-20 16:14:38
 */
@Api(tags = "后管-商家评论controller")
@AuthAdmin
@RestController
@RequestMapping("admin/merchantComment")
public class MerchantCommentController {
    /**
     * 服务对象
     */
    @Resource
    private MerchantCommentService merchantCommentService;

    @ApiOperation("新增商家评论")
    @PostMapping
    public MerchantCommentVO save(@RequestBody MerchantCommentDTO merchantCommentDTO) {
        return this.merchantCommentService.save(merchantCommentDTO);
    }

}

