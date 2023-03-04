package com.msb.mall.comment.enums;

import com.msb.framework.common.model.IDict;

/**
 * 评论标签
 *
 * @author shumengjiao
 */
public enum CommentLabelEnum implements IDict<Integer> {
    //
    SHARE_PICTURES(1, "晒图"),
    FOLLOW_COMMENT(2, "追评"),
    ;

    CommentLabelEnum(Integer code, String text) {
        init(code, text);
    }
}
