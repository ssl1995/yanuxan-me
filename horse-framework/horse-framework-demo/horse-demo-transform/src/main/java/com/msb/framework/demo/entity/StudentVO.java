package com.msb.framework.demo.entity;

import com.msb.framework.common.model.IDict;
import com.msb.framework.web.transform.annotation.TransformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author luozhan
 * @date 2022-08
 */
@Data
@SuppressWarnings("all")
@ApiModel(description = "学生对象")
public class StudentVO {
    //    private Long id;
    // 名字
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty("姓名")
    private String name;

    // 班干部-枚举（1-班长，2-副班长，3-学习委员，0-普通成员）
////    @NotNull
//    @ApiModelProperty("班干部")
//    @ApiModelPropertyEnum(dictEnum = ClassLeaderEnum.class)
//    private Integer classLeader;

    //  @Transform(transformer = EnumTransformer.class)
//    @Transform(from = "classLeader", transformer = EnumTransformer.class)
    @TransformEnum(ClassLeaderEnum.class)
    private String classLeaderName;

    // 性别-数据字典表（0-男，1-女）
    private Integer sex;
//    //      @TransformDict(dictGroup="sex")
////    @Transform(from = "sex", transformer = DictTransformer.class, group = "sex")
//    @TransformDict(group = "sex")
//    private String sexName;
//
//    // 班级-需通过班级信息进行转换（class表根据id查询name）
//    private Long classId;
//    @TransformClass
//    // @Transform(from = "classId", transformer = ClassTransformer.class)
//    private String className;
//    @Transform
//    private List<StudentVO> team;
//    @Transform
//    private StudentVO deskmate;


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
