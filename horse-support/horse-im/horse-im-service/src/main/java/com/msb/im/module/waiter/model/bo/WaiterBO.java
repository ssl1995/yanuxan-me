package com.msb.im.module.waiter.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: zhou miao
 * @create: 2022/05/10
 */

/**
 * 用户和客服实时绑定bo
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaiterBO {
    private String userId;
    private String waiterNickname;
    private String account;
    private String nickname;
    private Boolean isEnable;
}
