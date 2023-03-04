package com.msb.mall.product.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.api.model.VirtualProductDO;
import com.msb.mall.product.model.entity.VirtualProduct;
import com.msb.mall.product.model.vo.admin.VirtualProductVO;
import com.msb.mall.product.model.dto.admin.VirtualProductModifyDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 虚拟商品表(VirtualProduct)表服务接口
 *
 * @author shumengjiao
 * @since 2022-05-25 10:13:39
 */
@Mapper(componentModel = "spring")
public interface VirtualProductConvert {

    VirtualProductVO toVo(VirtualProduct virtualProduct);

    List<VirtualProductVO> toVo(List<VirtualProduct> virtualProduct);

    Page<VirtualProductVO> toVo(Page<VirtualProduct> virtualProduct);

    VirtualProduct toEntity(VirtualProductModifyDTO virtualProductModifyDTO);

    List<VirtualProduct> toEntity(List<VirtualProductModifyDTO> virtualProductModifyDTOList);

    List<VirtualProductDO> toDo(List<VirtualProductVO> virtualProductVOS);
}

