package com.msb.mall.marketing.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.marketing.client.EmployeeDubboClient;
import com.msb.mall.marketing.mapper.AppMessagePushMapper;
import com.msb.mall.marketing.model.dto.AppMessagePushDTO;
import com.msb.mall.marketing.model.dto.AppMessagePushQueryDTO;
import com.msb.mall.marketing.model.entity.AppMessagePush;
import com.msb.mall.marketing.model.vo.AppMessagePushVO;
import com.msb.mall.marketing.service.convert.AppMessagePushConvert;
import com.msb.user.api.vo.EmployeeDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * app消息推送(AppMessagePush)表服务实现类
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@Service("appMessagePushService")
public class AppMessagePushService extends ServiceImpl<AppMessagePushMapper, AppMessagePush> {

    @Resource
    private AppMessagePushConvert appMessagePushConvert;

    @Resource
    private ImSendMessageService imSendMessageService;

    @Resource
    private EmployeeDubboClient employeeDubboClient;

    public IPage<AppMessagePushVO> page(AppMessagePushQueryDTO appMessagePushDTO) {
        Page<AppMessagePush> page = this.lambdaQuery().like(StringUtils.isNotBlank(appMessagePushDTO.getTitle()), AppMessagePush::getTitle, appMessagePushDTO.getTitle())
                .between(Objects.nonNull(appMessagePushDTO.getEndReleaseTime()), AppMessagePush::getReleaseTime, appMessagePushDTO.getStartReleaseTime(), appMessagePushDTO.getEndReleaseTime())
                .page(appMessagePushDTO.page());
        Page<AppMessagePushVO> appMessagePushPageVO = appMessagePushConvert.toVo(page);

        Map<Long, EmployeeDO> employeeMap = employeeDubboClient.listEmployeeByUserId(page.getRecords());

        appMessagePushPageVO.getRecords().forEach(appMessagePushVO ->
                        Optional.ofNullable(employeeMap.get(appMessagePushVO.getCreateUser()))
                                .ifPresent(employeeDO -> appMessagePushVO.setCreateUserName(employeeDO.getEmployeeName())));
        return appMessagePushPageVO;
    }

    public AppMessagePushVO getOne(Serializable id) {
        return appMessagePushConvert.toVo(this.getById(id));
    }

    public Boolean save(AppMessagePushDTO appMessagePushDTO) {
        this.save(appMessagePushConvert.toDo(appMessagePushDTO));
        imSendMessageService.sendMarketingMessage(appMessagePushDTO);
        return true;
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

