package com.msb.framework.web.valid;

import javax.validation.groups.Default;

/**
 * 默认校验分组
 * 常见于新增和修改时，对于同一个DTO某些字段校验规则不同的场景
 * <p>
 * 使用方式：
 * 1.方法参数中使用 @Validated(ValidGroup.Save.class)
 * 2.校验属性上使用 @NotNull(groups=ValidGroup.Save.class)
 *
 * @author luozhan
 * @date 2022-8-8
 */
public interface ValidGroup {
    /**
     * 新增
     */
    interface Save extends Default {
    }

    /**
     * 更新
     */
    interface Update extends Default {
    }
}
