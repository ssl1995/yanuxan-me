package com.msb.pay.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.pay.model.dto.AppInfoDTO;
import com.msb.pay.model.dto.UpdateAppInfoDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.vo.AppInfoPageVO;
import com.msb.pay.model.vo.AppInfoVO;
import com.msb.pay.model.vo.AppSelectorVO;
import com.msb.pay.model.vo.AppSimpleInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 应用信息表(AppInfo)表服务接口
 *
 * @author makejava
 * @date 2022-06-06 10:42:42
 */
@Mapper(componentModel = "spring")
public interface AppInfoConvert {

    Page<AppInfoPageVO> toAppInfoPage(Page<AppInfo> page);

    @Mapping(target = "appPrimaryId", source = "id")
    AppInfoPageVO toAppInfoPageVO(AppInfo appInfo);

    @Mapping(target = "appPrimaryId", source = "id")
    AppInfoVO toAppInfoVO(AppInfo appInfo);

    AppInfo toAppInfo(AppInfoDTO appInfoDTO);

    @Mapping(target = "id", source = "appPrimaryId")
    AppInfo toAppInfo(UpdateAppInfoDTO appInfoDTO);

    @Mapping(target = "appPrimaryId", source = "id")
    AppSelectorVO toAppSelectorVO(AppInfo appInfo);

    List<AppSelectorVO> toAppSelectorVOList(List<AppInfo> list);

    @Mapping(target = "appPrimaryId", source = "id")
    AppSimpleInfoVO toAppSimpleInfoVO(AppInfo appInfo);

}

