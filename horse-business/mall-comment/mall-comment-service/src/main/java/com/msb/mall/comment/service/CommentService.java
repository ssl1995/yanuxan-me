package com.msb.mall.comment.service;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.redis.RedisClient;
import com.msb.framework.web.result.BizAssert;
import com.msb.like.api.dubbo.LikesDubboService;
import com.msb.like.api.enums.ScenesEnum;
import com.msb.like.api.enums.SystemEnum;
import com.msb.like.api.model.LikesDO;
import com.msb.like.api.model.LikesModifyDO;
import com.msb.like.api.model.LikesQueryDO;
import com.msb.mall.comment.api.model.CommentDO;
import com.msb.mall.comment.api.model.CommentDefaultDTO;
import com.msb.mall.comment.enums.CommentLabelEnum;
import com.msb.mall.comment.enums.CommentTypeEnum;
import com.msb.mall.comment.enums.OrderStatusEnum;
import com.msb.mall.comment.enums.SortTypeEnum;
import com.msb.mall.comment.mapper.CommentMapper;
import com.msb.mall.comment.model.constant.CommentConstants;
import com.msb.mall.comment.model.constant.RedisKeysConstants;
import com.msb.mall.comment.model.dto.CommentModifyDTO;
import com.msb.mall.comment.model.dto.admin.CommentAdminQueryDTO;
import com.msb.mall.comment.model.dto.admin.CommentSimpleDTO;
import com.msb.mall.comment.model.dto.app.CommentAppQueryDTO;
import com.msb.mall.comment.model.dto.app.CommentLikeDTO;
import com.msb.mall.comment.model.entity.Comment;
import com.msb.mall.comment.model.vo.CommentAnswerVO;
import com.msb.mall.comment.model.vo.CommentDetailVO;
import com.msb.mall.comment.model.vo.CommentFollowVO;
import com.msb.mall.comment.model.vo.CommentSimpleVO;
import com.msb.mall.comment.model.vo.admin.CommentAdminVO;
import com.msb.mall.comment.model.vo.admin.MerchantCommentVO;
import com.msb.mall.comment.model.vo.app.CommentAppVO;
import com.msb.mall.comment.model.vo.app.CommentCountVO;
import com.msb.mall.comment.model.vo.app.CommentLabelVO;
import com.msb.mall.comment.model.vo.app.ProductSatisfactionVO;
import com.msb.mall.comment.service.convert.CommentConvert;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.product.api.model.ProductDO;
import com.msb.mall.product.api.model.ProductDTO;
import com.msb.mall.product.api.model.ProductSkuDO;
import com.msb.mall.trade.api.dubbo.TradeOrderDubboService;
import com.msb.mall.trade.api.dubbo.TradeOrderProductDubboService;
import com.msb.mall.trade.api.enums.CommentStatusEnum;
import com.msb.mall.trade.api.model.TradeOrderProductDO;
import com.msb.sensitive.api.dubbo.SensitiveWordsDubboService;
import com.msb.sensitive.api.model.SensitiveWordsDO;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.dto.ListUserDTO;
import com.msb.user.api.vo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (Comment)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-13 21:08:56
 */
@Service("commentService")
@Slf4j
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CommentConvert commentConvert;
    @Resource
    private CommentExtraService commentExtraService;
    @Resource
    private RedisClient redisClient;
    @Resource
    private MerchantCommentService merchantCommentService;

    @DubboReference
    private UserDubboService userDubboService;
    @DubboReference
    private ProductDubboService productDubboService;
    @DubboReference
    private TradeOrderProductDubboService tradeOrderProductDubboService;
    @DubboReference
    private SensitiveWordsDubboService sensitiveWordsDubboService;
    @DubboReference
    private TradeOrderDubboService tradeOrderDubboService;
    @DubboReference
    private LikesDubboService likesDubboService;

    /**
     * 分页查询
     *
     * @param commentAdminQueryDTO 评论查询dto
     * @return 评论信息
     */
    public IPage<CommentAdminVO> page(CommentAdminQueryDTO commentAdminQueryDTO) {
        // 根据用户昵称模糊查询用户id
        List<Long> userIdList = null;
        if (StrUtil.isNotBlank(commentAdminQueryDTO.getUserName())) {
            ListUserDTO listUserDTO = new ListUserDTO().setNickname(commentAdminQueryDTO.getUserName());
            List<UserDO> userList = userDubboService.listUser(listUserDTO);
            BizAssert.notNull(userList, "没有该昵称的用户");
            userIdList = userList.stream().map(UserDO::getId).collect(Collectors.toList());
        }
        // 根据手机号查询出用户信息
        Long userIdByPhone = null;
        if (StrUtil.isNotBlank(commentAdminQueryDTO.getPhone())) {
            UserDO userByPhone = userDubboService.getUserByPhone(commentAdminQueryDTO.getPhone());
            BizAssert.notNull(userByPhone, "不存在该手机号的用户");
            userIdByPhone = userByPhone.getId();
        }
        // 根据商品名称模糊查询出productId 再根据productId查询订单详情id
        List<Long> orderProductIdList = null;
        if (StrUtil.isNotBlank(commentAdminQueryDTO.getProductName())) {
            ProductDTO productDTO = new ProductDTO().setName(commentAdminQueryDTO.getProductName());
            List<ProductDO> productList = productDubboService.listProduct(productDTO);
            List<Long> productIdList = productList.stream().map(ProductDO::getId).collect(Collectors.toList());
            List<TradeOrderProductDO> orderProductDOList = tradeOrderProductDubboService.listTradeOrderByProductId(productIdList);
            BizAssert.notNull(orderProductDOList, "没有该商品名称");
            orderProductIdList = orderProductDOList.stream().map(TradeOrderProductDO::getId).collect(Collectors.toList());
        }
        Page<Comment> page = this.lambdaQuery()
                .eq(Objects.nonNull(userIdByPhone), Comment::getUserId, userIdByPhone)
                .in(CollectionUtils.isNotEmpty(userIdList), Comment::getUserId, userIdList)
                .in(CollectionUtils.isNotEmpty(orderProductIdList), Comment::getOrderProductId, orderProductIdList)
                .in(CollectionUtils.isNotEmpty(commentAdminQueryDTO.getCommentScoreList()), Comment::getCommentScore, commentAdminQueryDTO.getCommentScoreList())
                .ge(Objects.nonNull(commentAdminQueryDTO.getCommentTimeBegin()), Comment::getCreateTime, commentAdminQueryDTO.getCommentTimeBegin())
                .le(Objects.nonNull(commentAdminQueryDTO.getCommentTimeEnd()), Comment::getCreateTime, commentAdminQueryDTO.getCommentTimeEnd())
                .eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode())
                .eq(Objects.nonNull(commentAdminQueryDTO.getIsShow()), Comment::getIsShow, commentAdminQueryDTO.getIsShow())
                .eq(Comment::getIsDeleted, false)
                .orderByDesc(Comment::getCreateTime)
                .page(commentAdminQueryDTO.page());
        // 查询商品信息
        List<Comment> records = page.getRecords();
        List<Long> productIdList = records.stream().map(Comment::getProductId).collect(Collectors.toList());
        // 查询用户信息
        List<Long> userIds = page.getRecords().stream().map(Comment::getUserId).collect(Collectors.toList());
        List<ProductDO> productList = Collections.emptyList();
        List<UserDO> userList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(records)) {
            productList = productDubboService.listProductById(productIdList);
            userList = userDubboService.listUserByIds(userIds);
        }
        Map<Long, ProductDO> productMap = productList.stream().collect(Collectors.toMap(ProductDO::getId, product -> product));
        Map<Long, UserDO> userMap = userList.stream().collect(Collectors.toMap(UserDO::getId, user -> user));
        Page<CommentAdminVO> commentAdminVoPage = commentConvert.toAdminVO(page);
        commentAdminVoPage.setRecords(commentAdminVoPage.getRecords().stream().map(p -> {
            // 添加商品信息
            if (productMap.containsKey(p.getProductId())) {
                p.setProductName(productMap.get(p.getProductId()).getName());
            }
            // 手机号 用户昵称
            if (userMap.containsKey(p.getUserId())) {
                p.setPhone(encryptPhone(userMap.get(p.getUserId()).getPhone()));
                p.setUserName(userMap.get(p.getUserId()).getNickname());
            }
            return p;
        }).collect(Collectors.toList()));
        return commentAdminVoPage;
    }

    /**
     * 查询评论详情
     *
     * @param id         评论id
     * @param isMerchant 是否商家查询
     * @return 评论详情vo
     */
    public CommentDetailVO getDetail(Serializable id, Boolean isMerchant) {
        // 查询评论信息
        Comment comment = this.lambdaQuery().eq(Comment::getId, id).eq(Comment::getIsDeleted, false).one();
        BizAssert.notNull(comment, "找不到该评论");
        // 查询商品信息
        ProductDO product = productDubboService.getProductById(comment.getProductId());
        // 查询商品规格
        TradeOrderProductDO tradeOrderProduct = tradeOrderProductDubboService.getTradeOrderProduct(comment.getOrderProductId());
        BizAssert.notNull(tradeOrderProduct, "订单有误");
        ProductSkuDO productSku = productDubboService.getProductSku(tradeOrderProduct.getProductSkuId());

        // 根据评论id查询追评
        Comment followComment = this.lambdaQuery()
                .eq(Comment::getCommentType, CommentTypeEnum.FOLLOW_COMMENT.getCode())
                .eq(Comment::getOriginId, id)
                .eq(Comment::getIsDeleted, false)
                .one();
        // 根据评论id查询回复
        List<Comment> answerCommentList = this.lambdaQuery()
                .eq(Comment::getCommentType, CommentTypeEnum.ANSWER_COMMENT.getCode())
                .eq(Comment::getOriginId, id)
                .eq(Comment::getIsDeleted, false)
                .list();
        if (CollectionUtils.isEmpty(answerCommentList)) {
            answerCommentList = Collections.emptyList();
        }
        // 查询商家评论
        MerchantCommentVO merchantCommentVo = merchantCommentService.getMerchantCommentByCommentId(comment.getId());

        // 获取评论和回复的用户信息
        Map<Long, UserDO> userInfoMap = getUserInfoMap(Collections.singletonList(comment), answerCommentList);
        // 获取此评论的用户信息
        UserDO userInfo = getUserInfo(userInfoMap, comment.getUserId());

        // 未过滤的回复对应的userId
        Map<Long, Long> answerUserMap = answerCommentList.stream().collect(Collectors.toMap(Comment::getId, Comment::getUserId));

        if (Boolean.FALSE.equals(isMerchant)) {
            // 不是后管查询 过滤评论显示状态为不展示且不是当前用户的回复
            answerCommentList = filterCommentByScore(answerCommentList);
        }

        // 转换vo
        CommentDetailVO commentDetail = commentConvert.toDetailVo(comment);
        CommentFollowVO followCommentDetail = commentConvert.toFollowVo(followComment);
        List<CommentAnswerVO> answerCommentDetailList = commentConvert.toAnswerVo(answerCommentList);
        // 给回复设置父userId
        for (CommentAnswerVO commentAnswerVO : answerCommentDetailList) {
            Long parentId = commentAnswerVO.getParentId();
            if (answerUserMap.containsKey(parentId)) {
                Long parentUserId = answerUserMap.get(parentId);
                commentAnswerVO.setParentUserId(parentUserId);
            }
        }
        // 时间倒序排序
        answerCommentDetailList = answerCommentDetailList.stream().sorted(Comparator.comparing(CommentAnswerVO::getCreateTime).reversed()).collect(Collectors.toList());

        if (followCommentDetail != null) {
            // 给追评设置用户信息
            followCommentDetail.setUserName(userInfo.getNickname());
        }

        // 给回复设置用户信息和parent用户信息
        setUserInfoToAnswerComment(answerCommentDetailList, Collections.singletonList(comment), userInfoMap);

        // 设置点赞
        setIsLike(commentDetail);

        // 设置回复数量
        int replyCount = answerCommentList.size();
        if (merchantCommentVo != null) {
            replyCount = replyCount + 1;
        }
        commentDetail.setReplyCount(replyCount);
        commentDetail.setUserName(userInfo.getNickname());
        commentDetail.setUserAvatar(userInfo.getAvatar());
        commentDetail.setPhone(encryptPhone(userInfo.getPhone()));
        commentDetail.setProductName(product.getName());
        commentDetail.setProductPicture(product.getMainPicture());
        commentDetail.setProductId(product.getId());
        commentDetail.setSkuName(productSku.getSkuName());
        commentDetail.setOrderProductId(comment.getOrderProductId());
        commentDetail.setFollowComment(followCommentDetail);
        commentDetail.setAnswerCommentList(answerCommentDetailList);
        commentDetail.setMerchantComment(merchantCommentVo);

        return commentDetail;
    }

    /**
     * 设置是否点赞
     *
     * @param commentDetailVO
     */
    public void setIsLike(CommentDetailVO commentDetailVO) {
        commentDetailVO.setIsLike(false);
        // 查询是否点赞
        boolean login = UserContext.isLogin();
        if (login) {
            Long userId = UserContext.getUserId();
            LikesQueryDO likesQueryDO = new LikesQueryDO()
                    .setSystemId(SystemEnum.YANXUAN.getCode())
                    .setScenesId(ScenesEnum.PRODUCT_COMMENT.getCode())
                    .setBusinessId(commentDetailVO.getId())
                    .setUserId(userId);
            Boolean isLike = likesDubboService.judgeSingleIsLike(likesQueryDO);
            commentDetailVO.setIsLike(isLike);
        }
    }

    /**
     * 根据用户id从map中获取用户昵称
     *
     * @param userMap 用户信息map
     * @param userId  用户id
     * @return 用户昵称
     */
    public String getUserName(Map<Long, UserDO> userMap, Long userId) {
        if (userMap.containsKey(userId)) {
            return userMap.get(userId).getNickname();
        }
        return StringUtil.EMPTY;
    }

    /**
     * 根据用户id从map中获取用户信息
     *
     * @param userMap 用户信息map
     * @param userId  用户id
     * @return 用户信息
     */
    public UserDO getUserInfo(Map<Long, UserDO> userMap, Long userId) {
        if (userMap.containsKey(userId)) {
            return userMap.get(userId);
        }
        return new UserDO();
    }

    /**
     * 保存评论、追评、回复
     *
     * @param commentModifyDTO 评论dto
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public CommentSimpleVO save(CommentModifyDTO commentModifyDTO) {
        String commentContent = commentModifyDTO.getCommentContent();
        if (StrUtil.isBlank(commentContent)) {
            throw new BizException("评论内容不能为空");
        }
        int length = commentContent.length();
        BizAssert.isTrue(length <= 500, "评论字数过长");
        // 检查敏感词
        SensitiveWordsDO sensitiveWords = sensitiveWordsDubboService.checkSensitiveWordsAndReplace(commentContent);
        BizAssert.notTrue(sensitiveWords.getIsContainSensitiveWords(), "您发表的信息[" + sensitiveWords.getContent() + "]包含敏感词内容，请修改后再发表。");
        // 检查是否已评论或者已追评
        checkIfCommented(commentModifyDTO);
        // 获取评分，评论类型为评论且2星及以下默认不显示
        if (CommentTypeEnum.NORMAL_COMMENT.getCode().equals(commentModifyDTO.getCommentType())
                && CommentConstants.CRITICAL_SCORE.compareTo(commentModifyDTO.getCommentScore()) >= 0) {
            commentModifyDTO.setIsShow(false);
        }

        Comment comment = commentConvert.toEntity(commentModifyDTO);

        // 类型为评论
        if (CommentTypeEnum.NORMAL_COMMENT.getCode().equals(comment.getCommentType())) {
            // 类型为评论 且有上传图片 添加晒图标签
            if (StrUtil.isNotBlank(comment.getPictureUrl())) {
                comment.setLabel(CommentLabelEnum.SHARE_PICTURES.getCode().toString());
            }
            // 修改订单状态
            changeOrderStatus(comment.getOrderProductId(), CommentTypeEnum.NORMAL_COMMENT.getCode());
            // 更新订单详情状态
            tradeOrderProductDubboService.updateTradeOrderProductCommentStatus(comment.getOrderProductId(), CommentStatusEnum.WAITE_FOLLOW.getCode());
        }
        // 类型为追评
        if (CommentTypeEnum.FOLLOW_COMMENT.getCode().equals(comment.getCommentType())) {
            // 添加评价标签
            String label = CommentLabelEnum.FOLLOW_COMMENT.getCode().toString();
            if (StrUtil.isNotBlank(comment.getPictureUrl())) {
                label = label + "," + CommentLabelEnum.SHARE_PICTURES.getCode().toString();
            }
            updateLabel(comment, label);
            // 修改订单状态
            changeOrderStatus(comment.getOrderProductId(), CommentTypeEnum.FOLLOW_COMMENT.getCode());
            // 更新订单详情状态
            tradeOrderProductDubboService.updateTradeOrderProductCommentStatus(comment.getOrderProductId(), CommentStatusEnum.FOLLOWED.getCode());
        }

        // 获取用户id
        Long userId = UserContext.getUserId();
        comment.setUserId(userId);
        // 保存评论
        this.save(comment);

        // 如果新增的类型是评论 则需要更新排序
        if (CommentTypeEnum.NORMAL_COMMENT.getCode().equals(commentModifyDTO.getCommentType())) {
            commentExtraService.sortCommentByDefaultAndToRedis(commentModifyDTO.getProductId());
        }
        // 获取新增的评论并返回
        return commentConvert.toVO(this.getById(comment.getId()));
    }

    /**
     * 修改订单状态
     *
     * @param orderProductId 订单详情id
     * @param commentType    评论类型
     */
    public void changeOrderStatus(Long orderProductId, Integer commentType) {
        TradeOrderProductDO tradeOrderProduct = tradeOrderProductDubboService.getTradeOrderProduct(orderProductId);
        Long orderId = tradeOrderProduct.getOrderId();
        // 查询该订单的其他订单详情信息是否都已评价或者追评
        Boolean result = checkIfAllComment(orderId, orderProductId, commentType);
        // 修改订单状态
        if (Boolean.TRUE.equals(result)) {
            // 评论 修改状态为：已完成
            if (CommentTypeEnum.NORMAL_COMMENT.getCode().equals(commentType)) {
                tradeOrderDubboService.updateOrderStatus(orderId, OrderStatusEnum.FINISH.getCode(), OrderStatusEnum.RECEIVING.getCode());
            }
            // 追评 修改状态为：已追评
            if (CommentTypeEnum.FOLLOW_COMMENT.getCode().equals(commentType)) {
                tradeOrderDubboService.updateOrderStatus(orderId, OrderStatusEnum.APPENDED.getCode(), OrderStatusEnum.FINISH.getCode());
            }
        }
    }

    /**
     * 判断是否该订单的每一条详情都已经评价或者追评
     *
     * @param orderId     订单id
     * @param commentType 评论类型
     * @return 是否都已评价或者追评
     */
    public Boolean checkIfAllComment(Long orderId, Long orderProductId, Integer commentType) {
        // 查询订单详情
        List<TradeOrderProductDO> orderProductDoList = tradeOrderProductDubboService.listTradeOrderProductByOrderId(orderId);
        // 获取除了当前评价之外的订单详情id
        List<Long> orderProductIdList = orderProductDoList.stream()
                .filter(op -> !op.getId().equals(orderProductId))
                .map(TradeOrderProductDO::getId).collect(Collectors.toList());
        // orderProductId为空说明该订单只有当前评价这一条订单详情
        if (CollectionUtils.isEmpty(orderProductIdList)) {
            return true;
        }
        // 除了当前评价的这条之外订单详情数量
        Integer count = orderProductIdList.size();
        // 判断是否该订单的所有订单详情都已评论或者追评
        Integer commentCount = this.lambdaQuery()
                .in(Comment::getOrderProductId, orderProductIdList)
                .eq(Comment::getCommentType, commentType)
                .eq(Comment::getIsDeleted, false).count();
        return commentCount.compareTo(count) == 0;
    }

    /**
     * 更新评价标签
     *
     * @param comment 评价信息
     * @param label   标签信息
     */
    public void updateLabel(Comment comment, String label) {
        // 获得追评的评价
        Comment one = this.lambdaQuery().eq(Comment::getId, comment.getOriginId()).eq(Comment::getIsDeleted, false).one();
        BizAssert.notNull(one, "该追评没有主评价");
        if (one.getLabel() != null) {
            label = one.getLabel() + "," + label;
        }
        // 更新评价标签
        this.lambdaUpdate().set(Comment::getLabel, label).eq(Comment::getId, one.getId()).update();
    }

    /**
     * check是否已评论或者已追评
     *
     * @param commentModifyDTO 评论dto
     */
    public void checkIfCommented(CommentModifyDTO commentModifyDTO) {
        // 查询是否已评论
        Integer commentType = commentModifyDTO.getCommentType();
        // 如果是回复，则不需要判断是否已存在，如果是评论或者追评只能有一条
        if (CommentTypeEnum.ANSWER_COMMENT.getCode().equals(commentType)) {
            return;
        }
        Long userId = UserContext.getUserId();
        Comment one = this.lambdaQuery()
                .eq(Comment::getUserId, userId)
                .eq(Comment::getOrderProductId, commentModifyDTO.getOrderProductId())
                .eq(Objects.nonNull(commentType), Comment::getCommentType, commentType)
                .eq(Comment::getIsDeleted, false).one();
        if (CommentTypeEnum.NORMAL_COMMENT.getCode().equals(commentType)) {
            BizAssert.isNull(one, "该订单详情已评论");
        }
        if (CommentTypeEnum.FOLLOW_COMMENT.getCode().equals(commentType)) {
            BizAssert.isNull(one, "该订单详情已追评");
        }

    }

    /**
     * 检查评论类型
     *
     * @param expectType 期待类型
     * @param actualType 实际类型
     */
    public void checkCommentType(Integer expectType, Integer actualType) {
        BizAssert.isTrue(expectType.compareTo(actualType) == 0, "评论类型不符合");
    }

    /**
     * 根据id更新评论、追评、回复
     *
     * @param commentSimpleDTO 更新dto
     * @return 更新结果
     */
    public Boolean updateShowStatus(CommentSimpleDTO commentSimpleDTO) {
        boolean update = this.lambdaUpdate().in(Comment::getId, commentSimpleDTO.getIdList()).set(Comment::getIsShow, commentSimpleDTO.getIsShow()).update();
        // 需要对每个商品的排序进行重排更新到redis中
        List<Comment> list = this.lambdaQuery().in(Comment::getId, commentSimpleDTO.getIdList()).groupBy(Comment::getProductId).list();
        for (Comment comment : list) {
            commentExtraService.sortCommentByDefaultAndToRedisAsync(comment.getProductId());
        }
        return update;
    }


    /**
     * 根据商品id查询评价
     *
     * @param productId 商品id
     * @return 评价信息
     */
    public List<Comment> listByProductId(Long productId) {
        BizAssert.notNull(productId, "productId不能为空");
        return this.lambdaQuery()
                .eq(Objects.nonNull(productId), Comment::getProductId, productId)
                .eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode())
                .eq(Comment::getIsDeleted, false)
                .list();
    }


    /**
     * 更新有用数
     *
     * @param commentLikeDTO 评价点赞dto
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUsefulCount(CommentLikeDTO commentLikeDTO) {
        // 保存点赞
        Long userId = UserContext.getUserId();
        LikesModifyDO likesModifyDO = new LikesModifyDO()
                .setSystemId(SystemEnum.YANXUAN.getCode())
                .setScenesId(ScenesEnum.PRODUCT_COMMENT.getCode())
                .setBusinessId(commentLikeDTO.getCommentId())
                .setIsLike(commentLikeDTO.getIsLike())
                .setUserId(userId);
        Boolean save = likesDubboService.addLike(likesModifyDO);
        BizAssert.isTrue(save, "点赞信息异常");
        // 更新评论点赞数
        Comment comment = this.lambdaQuery().eq(Comment::getId, commentLikeDTO.getCommentId()).one();
        Integer usefulCount = comment.getUsefulCount();
        if (Boolean.TRUE.equals(commentLikeDTO.getIsLike())) {
            usefulCount = usefulCount + 1;
        } else {
            usefulCount = usefulCount - 1;
        }
        Boolean update = this.lambdaUpdate().eq(Comment::getId, commentLikeDTO.getCommentId()).set(Comment::getUsefulCount, usefulCount).update();
        // 获取商品id
        Long productId = comment.getProductId();
        if (!CommentTypeEnum.NORMAL_COMMENT.getCode().equals(comment.getCommentType())) {
            productId = this.lambdaQuery().eq(Comment::getId, comment.getOriginId()).one().getProductId();
        }
        // 更新了评论信息需要更新排序
        commentExtraService.sortCommentByDefaultAndToRedisAsync(productId);
        return update;
    }

    /**
     * 查询评论标签
     *
     * @param productId 商品id
     * @return 评论标签
     */
    public List<CommentLabelVO> listCommentLabelByProductId(Long productId) {
        // 根据商品查询出所有评论
        List<Comment> commentList = listByProductId(productId);
        // 过滤评论显示状态为不展示且不是当前用户的评价
        commentList = filterCommentByScore(commentList);
        if (CollectionUtils.isEmpty(commentList)) {
            return Collections.emptyList();
        }
        // 查询有图片的评论
        Long sharePictureCount = commentList.stream().filter(item -> Optional.ofNullable(item.getLabel()).orElse("").contains(CommentLabelEnum.SHARE_PICTURES.getCode().toString()) || StrUtil.isNotBlank(item.getPictureUrl())).count();
        // 查询有追评的评价 originId在commentList中，且commentType=2
        List<Long> commentIdList = commentList.stream().map(Comment::getId).collect(Collectors.toList());
        Integer followCommentCount = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(commentIdList), Comment::getOriginId, commentIdList)
                .eq(Comment::getCommentType, CommentTypeEnum.FOLLOW_COMMENT.getCode())
                .count();
        ArrayList<CommentLabelVO> labelVoList = new ArrayList<>();
        CommentLabelVO pictureLabelVO = new CommentLabelVO().setLabelType(CommentLabelEnum.SHARE_PICTURES.getCode()).setCommentCount(sharePictureCount.intValue());
        CommentLabelVO followLabelVO = new CommentLabelVO().setLabelType(CommentLabelEnum.FOLLOW_COMMENT.getCode()).setCommentCount(followCommentCount);
        labelVoList.add(pictureLabelVO);
        labelVoList.add(followLabelVO);
        return labelVoList;
    }

    /**
     * app分页查询商品评论
     *
     * @param commentAppQueryDTO app查询dto
     * @return 分页查询结果
     */
    public IPage<CommentAppVO> pageAppComments(CommentAppQueryDTO commentAppQueryDTO) {

        // 排好序的评论
        List<Comment> commentList = listCommentBySort(commentAppQueryDTO);
        if (CollectionUtils.isEmpty(commentList)) {
            Page<CommentAppVO> page = commentAppQueryDTO.page();
            page.setRecords(commentConvert.toAppVo(commentList));
            return page;
        }
        // 评论id集合
        List<Long> commentIdList = commentList.stream().map(Comment::getId).collect(Collectors.toList());
        // 商品id集合
        List<Long> productIdList = commentList.stream().map(Comment::getProductId).collect(Collectors.toList());
        // 订单详情id集合
        List<Long> orderProductIdList = commentList.stream().map(Comment::getOrderProductId).collect(Collectors.toList());
        // 评论的用户id集合
        List<Long> userIdList = commentList.stream().map(Comment::getUserId).collect(Collectors.toList());

        // 获取评论的追评
        List<Comment> followCommentList = this.lambdaQuery().in(CollectionUtils.isNotEmpty(commentIdList), Comment::getOriginId, commentIdList).eq(Comment::getCommentType, CommentTypeEnum.FOLLOW_COMMENT.getCode()).eq(Comment::getIsDeleted, false).list();

        // 获取评论的回复
        List<Comment> answerCommentList = this.lambdaQuery().in(CollectionUtils.isNotEmpty(commentIdList), Comment::getOriginId, commentIdList).eq(Comment::getCommentType, CommentTypeEnum.ANSWER_COMMENT.getCode()).eq(Comment::getIsDeleted, false).list();

        // 获取用户信息 需要在用户过滤前查询用户信息
        Map<Long, UserDO> userMap = getUserInfoMap(commentList, answerCommentList);
        // 未过滤的回复对应的userId
        Map<Long, Long> answerUserMap = answerCommentList.stream().collect(Collectors.toMap(Comment::getId, comment -> comment.getUserId()));

        // 过滤评论显示状态为不展示且不是当前用户的追评
        followCommentList = filterCommentByScore(followCommentList);
        List<CommentFollowVO> followList = commentConvert.toFollowVo(followCommentList);

        // 过滤评论显示状态为不展示且不是当前用户的回复
        answerCommentList = filterCommentByScore(answerCommentList);
        // 已过滤用户的回复
        List<CommentAnswerVO> answerList = commentConvert.toAnswerVo(answerCommentList);

        // 获取商家回复
        List<MerchantCommentVO> merchantCommentVos = merchantCommentService.listByCommentIds(commentIdList);
        Map<Long, MerchantCommentVO> merchantCommentMap = merchantCommentVos.stream().collect(Collectors.toMap(MerchantCommentVO::getOriginId, merchantCommentVo -> merchantCommentVo));

        // 将用户信息设置给追评
        followList = setUserInfoToComment(followList, userMap);
        // 将追评转换成map
        Map<Long, CommentFollowVO> followMap = followList.stream().collect(Collectors.toMap(CommentFollowVO::getOriginId, comment -> comment));
        // 给回复设置父userId
        for (CommentAnswerVO commentAnswerVO : answerList) {
            Long parentId = commentAnswerVO.getParentId();
            if (answerUserMap.containsKey(parentId)) {
                Long parentUserId = answerUserMap.get(parentId);
                commentAnswerVO.setParentUserId(parentUserId);
            }
        }
        // 将用户信息设置给回复
        answerList = setUserInfoToAnswerComment(answerList, commentList, userMap);

        // 将回复转换成map
        Map<Long, List<CommentAnswerVO>> answerMap = answerList.stream().collect(Collectors.groupingBy(CommentAnswerVO::getOriginId));

        // 过滤评论显示状态为不展示且不是当前用户的评价
        commentList = filterCommentByScore(commentList);
        // 过滤没有内容的评论
        commentList = filterCommentByIfDefault(commentList, followCommentList, commentAppQueryDTO.getIsContent());
        // 过滤不是该标签的评论
        commentList = filterCommentByLabel(commentList, commentAppQueryDTO.getCommentLabel());

        // 获取sku信息
        Map<Long, ProductSkuDO> orderProductAndSkuMap = getSkuInfo(orderProductIdList);

        // 获取商品信息
        Map<Long, ProductDO> productMap = getProductNames(productIdList);

        // 获取点赞信息
        LikesQueryDO likesQueryDO = new LikesQueryDO().setSystemId(SystemEnum.YANXUAN.getCode()).setScenesId(ScenesEnum.PRODUCT_COMMENT.getCode()).setBusinessIdList(commentIdList).setUserIdList(Collections.singletonList(UserContext.getUserId()));
        List<LikesDO> likesDos = likesDubboService.listLike(likesQueryDO);
        Map<Long, LikesDO> likeMap = likesDos.stream().collect(Collectors.toMap(LikesDO::getBusinessId, Function.identity()));

        // 将追评、回复、用户信息、sku信息赋值给评论
        List<CommentAppVO> commentAppList = commentConvert.toAppVo(commentList);
        for (CommentAppVO commentApp : commentAppList) {
            // 追评
            if (followMap.containsKey(commentApp.getId())) {
                commentApp.setFollowComment(followMap.get(commentApp.getId()));
            }
            // 回复
            if (answerMap.containsKey(commentApp.getId())) {
                List<CommentAnswerVO> commentAnswerVOList = answerMap.get(commentApp.getId());
                commentAnswerVOList = commentAnswerVOList.stream().sorted(Comparator.comparing(CommentAnswerVO::getCreateTime).reversed()).collect(Collectors.toList());
                commentApp.setAnswerCommentList(commentAnswerVOList);
            }
            // 商家回复
            if (merchantCommentMap.containsKey(commentApp.getId())) {
                commentApp.setMerchantComment(merchantCommentMap.get(commentApp.getId()));
            }
            // 用户信息
            if (userMap.containsKey(commentApp.getUserId())) {
                commentApp.setUserName(userMap.get(commentApp.getUserId()).getNickname());
                commentApp.setUserAvatar(userMap.get(commentApp.getUserId()).getAvatar());
            }
            // 商品信息
            if (productMap.containsKey(commentApp.getProductId())) {
                commentApp.setProductName(productMap.get(commentApp.getProductId()).getName());
            }
            // sku信息
            if (orderProductAndSkuMap.containsKey(commentApp.getOrderProductId())) {
                ProductSkuDO productSku = orderProductAndSkuMap.get(commentApp.getOrderProductId());
                String skuName = productSku == null ? null : productSku.getSkuName();
                commentApp.setSkuName(skuName);
            }
            // 点赞信息
            commentApp.setIsLike(false);
            if (likeMap.containsKey(commentApp.getId())) {
                commentApp.setIsLike(true);
            }
            // 回复数量（商家回复、其他回复）
            Integer replyCount = 0;
            if (CollectionUtils.isNotEmpty(commentApp.getAnswerCommentList())) {
                replyCount = replyCount + commentApp.getAnswerCommentList().size();
            }
            if (commentApp.getMerchantComment() != null) {
                replyCount = replyCount + 1;
            }
            commentApp.setReplyCount(replyCount);
        }

        // 分页+筛除数据导致的数据条数不正确的问题，在查询时多查10条，最后返回时返回正确条数
        List<CommentAppVO> list = new ArrayList<>();
        for (int i = 0; i < commentAppQueryDTO.getLength(); i++) {
            int size = commentAppList.size();
            if (i + 1 > size) {
                break;
            }
            CommentAppVO commentAppVO = commentAppList.get(i);
            list.add(commentAppVO);
        }

        return setPageInfo(list, commentAppQueryDTO);
    }

    /**
     * 根据评论信息获取商品名称
     *
     * @param productIdList 商品id
     * @return 商品名称
     */
    public Map<Long, ProductDO> getProductNames(List<Long> productIdList) {
        if (CollectionUtils.isEmpty(productIdList)) {
            return Collections.emptyMap();
        }
        List<ProductDO> productList = productDubboService.listProductById(productIdList);
        return productList.stream().collect(Collectors.toMap(ProductDO::getId, product -> product));
    }

    /**
     * 根据评论信息获取sku信息
     *
     * @param orderProductIdList 评论列表
     * @return map<订单详情id, sku信息> map
     */
    public Map<Long, ProductSkuDO> getSkuInfo(List<Long> orderProductIdList) {
        if (CollectionUtils.isEmpty(orderProductIdList)) {
            return Collections.emptyMap();
        }
        // 查询订单详情信息
        List<TradeOrderProductDO> orderProductList = tradeOrderProductDubboService.listTradeOrderProduct(orderProductIdList);
        // 获取skuId列表
        List<Long> skuIdList = orderProductList.stream().map(TradeOrderProductDO::getProductSkuId).collect(Collectors.toList());
        // 查询sku信息
        List<ProductSkuDO> productSkuList = productDubboService.listProductSku(skuIdList);
        Map<Long, ProductSkuDO> skuMap = productSkuList.stream().collect(Collectors.toMap(ProductSkuDO::getSkuId, p -> p));
        // 组合sku信息和订单详情id
        return orderProductList.stream().collect(Collectors.toMap(TradeOrderProductDO::getId, p -> {
            if (skuMap.containsKey(p.getProductSkuId())) {
                return skuMap.get(p.getProductSkuId());
            }
            return new ProductSkuDO();
        }));
    }

    /**
     * 手机号脱敏处理
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public String encryptPhone(String phone) {
        phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone;
    }

    /**
     * 获取评论和回复的用户信息
     *
     * @param commentList       评论
     * @param answerCommentList 回复
     * @return 用户信息map
     */
    public Map<Long, UserDO> getUserInfoMap(List<Comment> commentList, List<Comment> answerCommentList) {
        if (CollectionUtils.isEmpty(commentList)) {
            commentList = Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(answerCommentList)) {
            answerCommentList = Collections.emptyList();
        }
        List<Long> userIdList = commentList.stream().map(Comment::getUserId).collect(Collectors.toList());
        List<Long> answerUserIdList = answerCommentList.stream().map(Comment::getUserId).collect(Collectors.toList());
        userIdList.addAll(answerUserIdList);
        List<UserDO> userList = userDubboService.listUserByIds(userIdList);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyMap();
        }
        return userList.stream().collect(Collectors.toMap(UserDO::getId, user -> user));
    }

    /**
     * 根据类型获取排序好的评论
     *
     * @param commentAppQueryDTO 入参
     * @return 排好序的评论
     */
    public List<Comment> listCommentBySort(CommentAppQueryDTO commentAppQueryDTO) {
        List<Comment> commentList = new ArrayList<>();
        // 默认排序 从redis中获取
        if (SortTypeEnum.DEFAULT_SORT.getCode().equals(commentAppQueryDTO.getSortType())) {
            commentList = listCommentFromRedis(commentAppQueryDTO.getProductId(), commentAppQueryDTO.getPageIndex(), commentAppQueryDTO.getLength());
        }
        // 时间倒序 从数据库获取
        if (SortTypeEnum.NEWEST_SORT.getCode().equals(commentAppQueryDTO.getSortType())) {
            commentList = commentMapper.listCommentSortByCreateTime(commentAppQueryDTO.page(), commentAppQueryDTO.getProductId());
        }
        // 没有上传排序规则
        if (commentAppQueryDTO.getSortType() == null) {
            commentList = commentMapper.listComment(commentAppQueryDTO.page(), commentAppQueryDTO.getProductId());
        }

        return commentList;
    }


    /**
     * 设置分页信息
     *
     * @param list               list
     * @param commentAppQueryDTO 入参
     * @return IPage<CommentAppVO>
     */
    public IPage<CommentAppVO> setPageInfo(List<CommentAppVO> list, CommentAppQueryDTO commentAppQueryDTO) {
        Page<CommentAppVO> commentAppVoPage = new Page<>();
        commentAppVoPage.setRecords(list);
        long total;
        String key = RedisKeysConstants.COMMENT_LIST_REDIS_KEY + commentAppQueryDTO.getProductId();

        Long size = redisClient.lSize(key);
        if (size % commentAppQueryDTO.getLength() == 1) {
            total = size / commentAppQueryDTO.getLength() + 1;
        } else {
            total = size / commentAppQueryDTO.getLength();
        }
        commentAppVoPage.setPages(total);
        commentAppVoPage.setTotal(size);
        return commentAppVoPage;
    }

    /**
     * 将用户信息添加到评论中
     *
     * @param list    评论列表
     * @param userMap 用户信息
     * @return 添加了用户信息的评论信息
     */
    public List<CommentFollowVO> setUserInfoToComment(List<CommentFollowVO> list, Map<Long, UserDO> userMap) {
        for (CommentFollowVO comment : list) {
            if (userMap.containsKey(comment.getUserId())) {
                UserDO user = userMap.get(comment.getUserId());
                comment.setUserName(user.getNickname());
            }
        }
        return list;
    }

    /**
     * 将用户信息添加到回复中
     *
     * @param answerList  回复列表
     * @param commentList 评论列表
     * @param userMap     用户信息
     * @return 添加了用户信息的评论信息
     */
    public List<CommentAnswerVO> setUserInfoToAnswerComment(List<CommentAnswerVO> answerList, List<Comment> commentList, Map<Long, UserDO> userMap) {
        // 将可能是父评论的id和userId对应存起来 评论和回复都有可能是父评论
        Map<Long, Long> parentOfCommentMap = commentList.stream().collect(Collectors.toMap(Comment::getId, Comment::getUserId, (v1, v2) -> v2));
        for (CommentAnswerVO comment : answerList) {
            comment.setUserName(getUserName(userMap, comment.getUserId()));
            Long parentId = comment.getParentId();
            // 设置回复的父评论昵称
            if (parentOfCommentMap.containsKey(parentId)) {
                String userName = getUserName(userMap, parentOfCommentMap.get(parentId));
                comment.setParentUserName(userName);
                continue;
            }
            Long parentUserId = comment.getParentUserId();
            String userName = getUserName(userMap, parentUserId);
            comment.setParentUserName(userName);
        }
        return answerList;
    }

    /**
     * 根据标签过滤评价
     *
     * @param list         评论
     * @param commentLabel 评论标签
     * @return 过滤后的评论
     */
    public List<Comment> filterCommentByLabel(List<Comment> list, Integer commentLabel) {
        if (commentLabel == null) {
            return list;
        }
        List<Comment> commentList = new ArrayList<>();
        for (Comment comment : list) {
            // 获取评论标签
            String label = comment.getLabel();
            if (StrUtil.isNotBlank(label)) {
                String[] split = label.split(",");
                List<String> stringList = Arrays.asList(split);
                // 判断评论标签中是否包含传入的标签
                boolean contains = stringList.contains(commentLabel.toString());
                if (contains) {
                    commentList.add(comment);
                }
            }
        }
        return commentList;
    }

    /**
     * 过滤没有内容的评价-默认评价且没有追评
     * 有内容的评价-不是默认评价或者是默认评价但是有追评
     *
     * @param list         评论
     * @param followList   追评
     * @param ifHasContent 是否有内容
     * @return 过滤后的评论
     */
    public List<Comment> filterCommentByIfDefault(List<Comment> list, List<Comment> followList, Boolean ifHasContent) {
        if (ifHasContent == null) {
            return list;
        }
        Map<Long, List<Comment>> followMap = followList.stream().collect(Collectors.groupingBy(Comment::getOriginId));

        Function<Comment, Boolean> function = comment -> !(Boolean.TRUE.equals(comment.getIsDefaultComment()) && !followMap.containsKey(comment.getId()));

        Map<Boolean, List<Comment>> commentMap = list.stream().collect(Collectors.groupingBy(function));
        return Optional.ofNullable(commentMap.get(ifHasContent)).orElse(Collections.emptyList());
    }

    /**
     * 过滤不展示且不是当前用户的评价
     *
     * @param list 评论
     * @return 过滤后的评论
     */
    public List<Comment> filterCommentByScore(List<Comment> list) {
        boolean login = UserContext.isLogin();
        log.info("登录状态：{}", login);
        List<Comment> commentList = new ArrayList<>();
        for (Comment comment : list) {
            // 如果没有登录 且评论为不显示 则不显示
            if (Boolean.FALSE.equals(login) && Boolean.FALSE.equals(comment.getIsShow())) {
                continue;
            }
            // 如果已登录 评论为不展示 且不是当前用户的评价
            if (Boolean.TRUE.equals(login) && Boolean.FALSE.equals(comment.getIsShow()) && !UserContext.getUserId().equals(comment.getUserId())) {
                continue;
            }
            commentList.add(comment);
        }
        return commentList;
    }


    /**
     * 从Redis中获取默认排序结果
     *
     * @param productId 商品id
     * @param pageIndex 当前页
     * @param length    长度
     * @return 排序结果
     */
    public List<Comment> listCommentFromRedis(Long productId, Integer pageIndex, Integer length) {
        // 处理数据条数问题，将数据多查10条
        // 查询的开始下标
        int start = (pageIndex - 1) * length;
        // 查询的结束下标
        int end = (pageIndex - 1) * length + length - 1;
        // 从Redis中获取评论
        String key = RedisKeysConstants.COMMENT_LIST_REDIS_KEY + productId;
        List<Integer> commentIdList = redisClient.lRange(key, start, end);
        if (CollectionUtils.isEmpty(commentIdList)) {
            // 如果redis中沒有则从数据库中查询
            commentExtraService.sortCommentByDefaultAndToRedis(productId);
            commentIdList = redisClient.lRange(key, start, end);
            if (commentIdList == null) {
                return new ArrayList<>();
            }
        }
        return commentIdList.stream().map(id -> lambdaQuery().eq(Comment::getId, id).one()).collect(Collectors.toList());
    }


    /**
     * 新增默认评价
     *
     * @param list 评价
     */
    public void addDefaultComments(List<CommentDefaultDTO> list) {
        List<Comment> commentList = commentConvert.toEntityDefault(list);
        List<Comment> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            // 判断该订单详情是否已存在评论
            Comment one = this.lambdaQuery()
                    .eq(Comment::getOrderProductId, comment.getOrderProductId())
                    .eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode())
                    .one();
            if (one != null) {
                continue;
            }
            comment.setCommentType(CommentTypeEnum.NORMAL_COMMENT.getCode())
                    .setIsDefaultComment(true)
                    .setCommentScore(CommentConstants.FULL_SCORE)
                    .setCommentContent(CommentConstants.DEFAULT_COMMENT_CONTENT);
            comments.add(comment);
        }
        this.saveBatch(comments);
        // 将数据新增到redis中
        for (CommentDefaultDTO commentDefaultDTO : list) {
            Long orderProductId = commentDefaultDTO.getOrderProductId();
            commentExtraService.sortCommentByDefaultAndToRedisAsync(orderProductId);
        }
    }

    /**
     * 根据订单id查询评论详情列表
     *
     * @param orderId 订单id
     * @return 评论详情列表
     */
    public List<CommentAppVO> listOrderCommentByOrderId(Long orderId) {
        // 查询订单详情
        List<TradeOrderProductDO> orderProductDoList = tradeOrderProductDubboService.listTradeOrderProductByOrderId(orderId);
        BizAssert.notNull(orderProductDoList, "订单信息有误");
        // 初始化一个评论信息
        List<CommentAppVO> commentAppVoList = orderProductDoList.stream()
                .map(item -> new CommentAppVO().setOrderProductId(item.getId()).setProductId(item.getProductId()))
                .collect(Collectors.toList());
        // 商品id集合
        List<Long> productIdList = orderProductDoList.stream().map(TradeOrderProductDO::getProductId).collect(Collectors.toList());
        // 订单详情id集合
        List<Long> orderProductIdList = orderProductDoList.stream().map(TradeOrderProductDO::getId).collect(Collectors.toList());
        // 查询评论列表
        List<Comment> commentList = this.lambdaQuery()
                .in(Comment::getOrderProductId, orderProductIdList)
                .eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode())
                .eq(Comment::getIsDeleted, false)
                .list();
        List<Long> commentIdList = commentList.stream().map(Comment::getId).collect(Collectors.toList());
        // 根据评论id查询追评
        List<Comment> followList = listCommentsByIdAndType(commentIdList, CommentTypeEnum.FOLLOW_COMMENT);
        // 根据评论id查询回复
        List<Comment> answerList = listCommentsByIdAndType(commentIdList, CommentTypeEnum.ANSWER_COMMENT);
        // 根据评论id查询商家回复
        List<MerchantCommentVO> merchantCommentVos = merchantCommentService.listByCommentIds(commentIdList);
        Map<Long, MerchantCommentVO> merchantMap = merchantCommentVos.stream().collect(Collectors.toMap(MerchantCommentVO::getOriginId, merchant -> merchant));
        // 获取用户信息
        Map<Long, UserDO> userMap = getUserInfoMap(commentList, answerList);
        // 转换vo
        List<CommentFollowVO> commentFollowVoList = commentConvert.toFollowVo(followList);
        List<CommentAnswerVO> commentAnswerVoList = commentConvert.toAnswerVo(answerList);
        Map<Long, Long> answerUserMap = commentAnswerVoList.stream().collect(Collectors.toMap(CommentAnswerVO::getId, CommentAnswerVO::getUserId));
        // 给回复设置父用户id
        for (CommentAnswerVO commentAnswerVO : commentAnswerVoList) {
            Long parentId = commentAnswerVO.getParentId();
            if (answerUserMap.containsKey(parentId)) {
                Long parentUserId = answerUserMap.get(parentId);
                commentAnswerVO.setParentUserId(parentUserId);
            }
        }
        // 将用户信息设置给追评
        commentFollowVoList = setUserInfoToComment(commentFollowVoList, userMap);
        Map<Long, CommentFollowVO> followMap = commentFollowVoList.stream().collect(Collectors.toMap(CommentFollowVO::getOriginId, commentFollowVo -> commentFollowVo));
        // 将用户信息设置给回复
        commentAnswerVoList = setUserInfoToAnswerComment(commentAnswerVoList, commentList, userMap);
        Map<Long, List<CommentAnswerVO>> answerMap = commentAnswerVoList.stream().collect(Collectors.groupingBy(CommentAnswerVO::getOriginId));
        // 获取sku信息
        Map<Long, ProductSkuDO> skuMap = getSkuInfo(orderProductIdList);
        // 获取商品信息
        Map<Long, ProductDO> productMap = getProductNames(productIdList);
        // 获取点赞数据
        Map<String, LikesDO> likeMap = getLikeMap(commentList);

        // 将追评、回复、用户信息、sku信息赋值给评论
        List<CommentAppVO> commentAppList = commentConvert.toAppVo(commentList);
        Map<Long, CommentAppVO> commentVoMap = commentAppList.stream().collect(Collectors.toMap(CommentAppVO::getOrderProductId, commentVo -> commentVo));
        // 对初始化的vo进行赋值
        for (CommentAppVO commentApp : commentAppVoList) {
            // 回复数量（商家回复、其他回复）
            int replyCount = 0;
            // 评论
            if (commentVoMap.containsKey(commentApp.getOrderProductId())) {
                CommentAppVO commentAppVo = commentVoMap.get(commentApp.getOrderProductId());
                setCommentValue(commentApp, commentAppVo);
            }
            // 追评
            if (followMap.containsKey(commentApp.getId())) {
                commentApp.setFollowComment(followMap.get(commentApp.getId()));
            }
            // 回复
            if (answerMap.containsKey(commentApp.getId())) {
                List<CommentAnswerVO> commentAnswerVOList = answerMap.get(commentApp.getId());
                commentAnswerVOList = commentAnswerVOList.stream().sorted(Comparator.comparing(CommentAnswerVO::getCreateTime).reversed()).collect(Collectors.toList());
                commentApp.setAnswerCommentList(commentAnswerVOList);
                int size = commentAnswerVOList.size();
                replyCount = replyCount + size;
            }
            // 商家回复
            if (merchantMap.containsKey(commentApp.getId())) {
                commentApp.setMerchantComment(merchantMap.get(commentApp.getId()));
                replyCount = replyCount + 1;
            }
            // 用户信息
            if (userMap.containsKey(commentApp.getUserId())) {
                commentApp.setUserName(userMap.get(commentApp.getUserId()).getNickname());
                commentApp.setUserAvatar(userMap.get(commentApp.getUserId()).getAvatar());
            }
            // 商品信息
            if (productMap.containsKey(commentApp.getProductId())) {
                commentApp.setProductName(productMap.get(commentApp.getProductId()).getName());
                commentApp.setProductPicture(productMap.get(commentApp.getProductId()).getMainPicture());
            }
            // sku信息
            if (skuMap.containsKey(commentApp.getOrderProductId())) {
                commentApp.setSkuName(skuMap.get(commentApp.getOrderProductId()).getSkuName());
            }
            // 点赞信息
            commentApp.setIsLike(false);
            // userId为空不需要再获取点赞信息
            if (commentApp.getUserId() == null) {
                continue;
            }
            commentApp.setIsLike(likeMap.containsKey(UserContext.getUserId() + "" + commentApp.getId()));
            // 回复数量
            commentApp.setReplyCount(replyCount);

        }
        // 排序 未评价的、未追评的、时间倒序
        return commentAppVoList.stream()
                .sorted(Comparator.comparing(CommentAppVO::getId, Comparator.nullsLast(Long::compareTo))
                        .thenComparing(CommentAppVO::getFollowComment, Comparator.nullsLast(Comparator.comparing(CommentFollowVO::getId)))
                        .thenComparing(CommentAppVO::getCreateTime, Comparator.nullsLast(LocalDateTime::compareTo)).reversed()
                ).collect(Collectors.toList());
    }

    /**
     * 设置commentValue
     *
     * @param resultVo   被设置的值
     * @param resourceVo 数据值
     */
    public void setCommentValue(CommentAppVO resultVo, CommentAppVO resourceVo) {
        resultVo.setId(resourceVo.getId())
                .setProductPicture(resourceVo.getProductPicture())
                .setUserId(resourceVo.getUserId())
                .setCommentContent(resourceVo.getCommentContent())
                .setCommentScore(resourceVo.getCommentScore())
                .setCommentType(resourceVo.getCommentType())
                .setCreateTime(resourceVo.getCreateTime())
                .setIsShow(resourceVo.getIsShow())
                .setPictureUrl(resourceVo.getPictureUrl())
                .setUsefulCount(resourceVo.getUsefulCount())
                .setOriginId(resourceVo.getOriginId())
                .setParentId(resourceVo.getParentId());
    }

    /**
     * 根据评论id和类型查询
     *
     * @param commentIdList   评论id
     * @param commentTypeEnum 评论类型
     * @return 评论信息
     */
    public List<Comment> listCommentsByIdAndType(List<Long> commentIdList, CommentTypeEnum commentTypeEnum) {
        if (CollectionUtils.isEmpty(commentIdList)) {
            return Collections.emptyList();
        }
        List<Comment> list = this.lambdaQuery()
                .eq(Comment::getCommentType, commentTypeEnum.getCode())
                .in(Comment::getOriginId, commentIdList)
                .eq(Comment::getIsDeleted, false)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * 获取likeMap
     *
     * @param list
     * @return
     */
    public Map<String, LikesDO> getLikeMap(List<Comment> list) {
        // 获取点赞数据
        List<Long> commentIdList = list.stream().map(Comment::getId).collect(Collectors.toList());
        LikesQueryDO likesQueryDO = new LikesQueryDO().setSystemId(SystemEnum.YANXUAN.getCode()).setScenesId(ScenesEnum.PRODUCT_COMMENT.getCode()).setBusinessIdList(commentIdList).setUserIdList(Collections.singletonList(UserContext.getUserId()));
        List<LikesDO> likesDOS = likesDubboService.listLike(likesQueryDO);
        return likesDOS.stream().collect(Collectors.toMap(like -> like.getUserId() + "" + like.getBusinessId(), like -> like));
    }

    /**
     * 根据订单详情id查询评论信息
     *
     * @param orderProductIdList 订单详情id
     * @return 评论信息
     */
    public List<CommentDO> listByOrderProductId(List<Long> orderProductIdList, Integer... commentType) {
        BizAssert.notNull(orderProductIdList, "订单详情id为空");
        List<Comment> commentList = this.lambdaQuery().in(Comment::getOrderProductId, orderProductIdList).in(Comment::getCommentType, Arrays.asList(commentType)).list();
        return commentConvert.toCommentDO(commentList);
    }

    /**
     * 查询商品评论总数量
     *
     * @param productId 商品id
     * @return 总数量
     */
    public CommentCountVO getAllCommentCount(Long productId) {
        // 获取是否登录
        boolean login = UserContext.isLogin();

        // 查询所有评价数量
        Integer allCommentCount = 0;
        List<Comment> allCommentList = this.lambdaQuery()
                .eq(Comment::getProductId, productId)
                .eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode())
                .eq(Comment::getIsDeleted, false)
                .list();

        for (Comment comment : allCommentList) {
            // 如果已登录，不是用户的且2星及以下评价隐藏
            if (Boolean.TRUE.equals(login) && Boolean.FALSE.equals(comment.getIsShow()) && !UserContext.getUserId().equals(comment.getUserId())) {
                continue;
            }
            // 如果未登录，所有用户的2星及以下的评论隐藏
            if (Boolean.FALSE.equals(login) && Boolean.FALSE.equals(comment.getIsShow())) {
                continue;
            }
            allCommentCount = allCommentCount + 1;
        }

        // 查询默认评价
        List<Comment> defaultCommentList = this.lambdaQuery()
                .eq(Comment::getProductId, productId)
                .eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode())
                .eq(Comment::getIsDefaultComment, true)
                .eq(Comment::getIsDeleted, false)
                .list();
        // 判断默认评价是否有追评
        Integer defaultCommentCount = 0;
        if (CollectionUtils.isNotEmpty(defaultCommentList)) {
            List<Long> idList = defaultCommentList.stream().map(Comment::getId).collect(Collectors.toList());
            List<Comment> followCommentList = this.lambdaQuery()
                    .eq(Comment::getCommentType, CommentTypeEnum.FOLLOW_COMMENT.getCode())
                    .in(Comment::getOriginId, idList)
                    .eq(Comment::getIsDeleted, false)
                    .list();
            Map<Long, Long> followMap = followCommentList.stream().collect(Collectors.toMap(Comment::getOriginId, Comment::getOriginId));
            // 默认评价且没有追评的 数量
            Long count = idList.stream().filter(id -> !followMap.containsKey(id)).count();
            defaultCommentCount = count.intValue();
        }

        return new CommentCountVO().setAllCommentCount(allCommentCount).setDefaultCommentCount(defaultCommentCount);
    }

    /**
     * 查询商品满意度
     * 商品评价>10条则计算满意度
     * 商品满意度 = 基础分值 + 用户评价总和 ÷ 评价数量 ×系数
     * 基础分值 3分   系数 40%
     *
     * @param productId 商品id
     */
    public ProductSatisfactionVO getProductSatisfaction(Long productId) {
        // 查询商品评价条数
        List<Comment> list = this.lambdaQuery().eq(Comment::getProductId, productId).eq(Comment::getCommentType, CommentTypeEnum.NORMAL_COMMENT.getCode()).list();
        if (CollectionUtils.isEmpty(list) || list.size() <= CommentConstants.CRITICAL_COMMENT_COUNT) {
            // 不计算
            return new ProductSatisfactionVO().setIsCalculateSatisfaction(false);
        }
        // 分值总和
        long sum = list.stream().mapToInt(Comment::getCommentScore).summaryStatistics().getSum();
        // 评价数量
        int commentCount = list.size();
        // 计算满意度
        BigDecimal satisfaction = BigDecimal.valueOf(CommentConstants.BASE_SCORE).add(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(commentCount), 4, RoundingMode.HALF_UP).multiply(CommentConstants.PRODUCT_SATISFACTION_COEFFICIENT));
        // 设置小数
        DecimalFormat df1 = new DecimalFormat("0.0");
        String productSatisfaction = df1.format(satisfaction);
        return new ProductSatisfactionVO().setIsCalculateSatisfaction(true).setProductSatisfaction(productSatisfaction);
    }
}

