package com.msb.mall.comment.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.mall.comment.model.dto.CommentModifyDTO;
import com.msb.mall.comment.model.dto.app.CommentAppQueryDTO;
import com.msb.mall.comment.model.dto.app.CommentLikeDTO;
import com.msb.mall.comment.model.vo.CommentDetailVO;
import com.msb.mall.comment.model.vo.CommentSimpleVO;
import com.msb.mall.comment.model.vo.app.CommentAppVO;
import com.msb.mall.comment.model.vo.app.CommentCountVO;
import com.msb.mall.comment.model.vo.app.CommentLabelVO;
import com.msb.mall.comment.model.vo.app.ProductSatisfactionVO;
import com.msb.mall.comment.service.CommentService;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * (Comment)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-13 21:08:50
 */
@Api(tags = "app-评论controller")
@RestController
@RequestMapping("app/comment")
public class CommentAppController {

    @Resource
    private CommentService commentService;

    @NoAuth
    @ApiOperation("分页查询商品评价")
    @GetMapping
    IPage<CommentAppVO> page(CommentAppQueryDTO commentAppQueryDTO) {
        return commentService.pageAppComments(commentAppQueryDTO);
    }

    @ApiOperation("新增评论")
    @PostMapping
    public CommentSimpleVO save(@RequestBody CommentModifyDTO commentModifyDTO) {
        return this.commentService.save(commentModifyDTO);
    }

    @ApiOperation("更新评价有用数")
    @PutMapping("updateUsefulCount")
    public Boolean updateUsefulCount(@RequestBody @Validated CommentLikeDTO commentLikeDTO) {
        return commentService.updateUsefulCount(commentLikeDTO);
    }

    @NoAuth
    @ApiOperation("根据商品id查询评论标签")
    @GetMapping("listCommentLabel/{productId}")
    @Transform
    public List<CommentLabelVO> listCommentLabel(@NotNull @PathVariable Long productId) {
        return commentService.listCommentLabelByProductId(productId);
    }

    @ApiOperation("根据订单id查询评论详情")
    @GetMapping("listOrderCommentByOrderId/{orderId}")
    public List<CommentAppVO> listOrderCommentByOrderId(@Null @PathVariable Long orderId) {
        return commentService.listOrderCommentByOrderId(orderId);
    }

    @NoAuth
    @ApiOperation("根据商品id查询全部评论数量")
    @GetMapping("getAllCommentCountByProductId/{productId}")
    public CommentCountVO getAllCommentCountByProductId(@NotNull @PathVariable Long productId) {
        return commentService.getAllCommentCount(productId);
    }

    @NoAuth
    @ApiOperation("根据评价id查询评价详情")
    @GetMapping("getCommentDetail/{commentId}")
    public CommentDetailVO getCommentDetail(@NotNull @PathVariable Long commentId) {
        return commentService.getDetail(commentId, false);
    }

    @NoAuth
    @ApiOperation("查询商品满意度")
    @GetMapping("getProductSatisfaction/{productId}")
    public ProductSatisfactionVO getProductSatisfaction(@NotNull @PathVariable Long productId) {
        return commentService.getProductSatisfaction(productId);
    }


}
