package com.msb.business.model.entity;

import com.msb.framework.common.model.IDict;
import lombok.Data;

/**
 * @author luozhan
 * @date 2022-08
 */
@Data
@SuppressWarnings("all")
public class StudentVO {
    private Long id;
    // 名字
    private String name;
    // 班干部-枚举（1-班长，2-副班长，3-学习委员，0-普通成员）
    private Integer classLeader;
    private String classLeaderName;
    // 性别-数据字典表（0-男，1-女）
    private Integer sex;
    private String sexName;
    // 班级-需班级表翻译（class表根据id查询name）
    private Long classId;
    private String className;

    public static enum ClassLeaderEnum implements IDict<Integer> {

        MONITOR(1, "班长"),
        VICE_MONITOR(2, "副班长"),
        STUDY(3, "学习委员"),
        NORMAL(0, "普通成员");

        ClassLeaderEnum(Integer code, String text) {
            init(code, text);
        }
    }
}
