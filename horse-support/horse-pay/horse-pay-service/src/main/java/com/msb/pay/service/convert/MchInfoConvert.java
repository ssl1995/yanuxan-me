package com.msb.pay.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.pay.model.dto.MchInfoDTO;
import com.msb.pay.model.dto.UpdateMchInfoDTO;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.vo.MchInfoPageVO;
import com.msb.pay.model.vo.MchInfoVO;
import com.msb.pay.model.vo.MchSelectorVO;
import com.msb.pay.model.vo.MchSimpleInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 商户信息表(MchInfo)表服务接口
 *
 * @author makejava
 * @date 2022-06-06 10:42:43
 */
@Mapper(componentModel = "spring")
public interface MchInfoConvert {

    Page<MchInfoPageVO> toMchInfoPage(Page<MchInfo> page);

    @Mapping(target = "mchPrimaryId", source = "id")
    MchInfoPageVO toMchInfoPageVO(MchInfo mchInfo);

    @Mapping(target = "mchPrimaryId", source = "id")
    MchInfoVO toMchInfoInfoVO(MchInfo mchInfo);

    MchInfo toMchInfo(MchInfoDTO mchInfoDTO);

    @Mapping(target = "id", source = "mchPrimaryId")
    MchInfo toMchInfo(UpdateMchInfoDTO updateMchInfoDTO);

    @Mapping(target = "mchPrimaryId", source = "id")
    MchSelectorVO toMchSelectorVO(MchInfo mchInfo);

    List<MchSelectorVO> toMchSelectorVOList(List<MchInfo> list);

    @Mapping(target = "mchPrimaryId", source = "id")
    MchSimpleInfoVO toMchSimpleVO(MchInfo mchInfo);

}

