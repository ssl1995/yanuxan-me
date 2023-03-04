package com.msb.mall.comment.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.comment.mapper.MerchantCommentMapper;
import com.msb.mall.comment.model.dto.admin.MerchantCommentDTO;
import com.msb.mall.comment.model.entity.MerchantComment;
import com.msb.mall.comment.model.vo.admin.MerchantCommentVO;
import com.msb.mall.comment.service.convert.MerchantCommentConvert;
import com.msb.sensitive.api.dubbo.SensitiveWordsDubboService;
import com.msb.user.api.EmployeeDubboService;
import com.msb.user.api.vo.EmployeeDO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家评论(MerchantComment)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-20 16:14:38
 */
@Service("merchantCommentService")
public class MerchantCommentService extends ServiceImpl<MerchantCommentMapper, MerchantComment> {

    @Resource
    private CommentService commentService;
    @Resource
    private MerchantCommentConvert merchantCommentConvert;
    @DubboReference
    private SensitiveWordsDubboService sensitiveWordsDubboService;
    @DubboReference
    private EmployeeDubboService employeeDubboService;

    /**
     * 保存商家评论
     *
     * @param merchantCommentDTO 商家评论信息
     * @return 评论信息
     */
    public MerchantCommentVO save(MerchantCommentDTO merchantCommentDTO) {
        // 检查该评论是否已有商家评论
        checkIfHasMerchantComment(merchantCommentDTO.getOriginId());

        MerchantComment merchantComment = merchantCommentConvert.toEntity(merchantCommentDTO);
        Long userId = UserContext.getUserId();
        merchantComment.setUserId(userId);

        this.save(merchantComment);
        return merchantCommentConvert.toVo(this.getById(merchantComment.getId()));
    }

    /**
     * 校验评论是否已存在商家评论
     *
     * @param commentId 评论id
     */
    public void checkIfHasMerchantComment(Long commentId) {
        MerchantComment one = this.lambdaQuery()
                .eq(MerchantComment::getOriginId, commentId)
                .eq(MerchantComment::getIsDeleted, false)
                .one();
        BizAssert.isNull(one, "该评论已存在商家评论");
    }

    /**
     * 根据评论id查询商家评论 不论是否显示
     *
     * @param commentId 评论id
     * @return 商家评论
     */
    public MerchantCommentVO getMerchantCommentByCommentId(Long commentId) {
        MerchantComment one = this.lambdaQuery()
                .eq(MerchantComment::getOriginId, commentId)
                .eq(MerchantComment::getIsDeleted, false)
                .one();
        MerchantCommentVO merchantCommentVo = merchantCommentConvert.toVo(one);
        // 查询商家用户信息
        if (merchantCommentVo != null) {
            merchantCommentVo.setUserName("马士兵严选");
        }
        return merchantCommentVo;
    }

    /**
     * 根据评论id获取商家回复 只查询显示的
     *
     * @param commentIdList 评论Id
     * @return 商家回复
     */
    public List<MerchantCommentVO> listByCommentIds(List<Long> commentIdList) {
        if (CollectionUtils.isEmpty(commentIdList)) {
            return Collections.emptyList();
        }
        List<MerchantComment> list = this.lambdaQuery()
                .in(MerchantComment::getOriginId, commentIdList)
                .eq(MerchantComment::getIsShow, true)
                .eq(MerchantComment::getIsDeleted, false)
                .list();
        // 获取商家回复的昵称
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<MerchantCommentVO> merchantCommentVos = merchantCommentConvert.toVo(list);
        for (MerchantCommentVO merchantCommentVo : merchantCommentVos) {
            merchantCommentVo.setUserName("商家回复");
        }
        return merchantCommentVos;
    }
}

