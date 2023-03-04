package com.msb.mall.comment.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.mall.comment.enums.CommentTypeEnum;
import com.msb.mall.comment.model.dto.CommentModifyDTO;
import com.msb.mall.comment.model.dto.admin.CommentAdminQueryDTO;
import com.msb.mall.comment.model.dto.admin.CommentSimpleDTO;
import com.msb.mall.comment.model.vo.CommentDetailVO;
import com.msb.mall.comment.model.vo.CommentSimpleVO;
import com.msb.mall.comment.model.vo.admin.CommentAdminVO;
import com.msb.mall.comment.service.CommentService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * (Comment)表控制层
 *
 * @author shumengjiao
 * @since 2022-06-13 21:08:50
 */
@Api(tags = "后管-评论controller")
@AuthAdmin
@RestController
@RequestMapping("admin/comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    @ApiOperation("分页查询评论")
    @GetMapping
    public IPage<CommentAdminVO> page(CommentAdminQueryDTO commentAdminQueryDTO) {
        return commentService.page(commentAdminQueryDTO);
    }

    @ApiOperation("查询评论详情")
    @GetMapping("getCommentDetail/{id}")
    public CommentDetailVO getCommentDetail(@NotNull @PathVariable Serializable id) {
        return this.commentService.getDetail(id, true);
    }

    @ApiOperation("新增评论")
    @PostMapping
    public CommentSimpleVO save(@RequestBody CommentModifyDTO commentModifyDTO) {
        // 后管只能新增回复
        commentService.checkCommentType(CommentTypeEnum.ANSWER_COMMENT.getCode(), commentModifyDTO.getCommentType());
        return this.commentService.save(commentModifyDTO);
    }

    @ApiOperation("更新显示状态")
    @PutMapping
    public Boolean updateShowStatus(@Validated @RequestBody CommentSimpleDTO commentSimpleDTO) {
        return this.commentService.updateShowStatus(commentSimpleDTO);
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Boolean delete(List<Long> idList) {
        return this.commentService.removeByIds(idList);
    }
}

