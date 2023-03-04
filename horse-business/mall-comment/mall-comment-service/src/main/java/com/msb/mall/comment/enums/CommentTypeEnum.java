package com.msb.mall.comment.enums;

import com.msb.framework.common.model.IDict;

/**
 * 评论类型
 *
 * @author shumengjiao
 */
public enum CommentTypeEnum implements IDict<Integer> {
    //
    NORMAL_COMMENT(1, "评论"),
    FOLLOW_COMMENT(2, "追评"),
    ANSWER_COMMENT(3, "回复"),
    ;

    CommentTypeEnum(Integer code, String text) {
        init(code, text);
    }
}
