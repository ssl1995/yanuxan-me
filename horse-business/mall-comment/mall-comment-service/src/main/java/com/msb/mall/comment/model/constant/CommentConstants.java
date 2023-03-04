package com.msb.mall.comment.model.constant;

import java.math.BigDecimal;

/**
 * @author 86151
 */
public class CommentConstants {
    /**
     * 评分临界值 2分及以下不显示
     */
    public static final Integer CRITICAL_SCORE = 2;
    /**
     * 满分分值 5分
     */
    public static final Integer FULL_SCORE = 5;
    /**
     * 基础分值 3分
     */
    public static final Integer BASE_SCORE = 3;

    /**
     * 默认评价
     */
    public static final String DEFAULT_COMMENT_CONTENT = "用户超时未做出评价，系统默认好评";

    /**
     * 评价数量临界值 10条及以下不计算商品满意度
     */
    public static final Integer CRITICAL_COMMENT_COUNT = 10;

    /**
     * 商品满意度系数
     */
    public static final BigDecimal PRODUCT_SATISFACTION_COEFFICIENT = BigDecimal.valueOf(0.4);
}
