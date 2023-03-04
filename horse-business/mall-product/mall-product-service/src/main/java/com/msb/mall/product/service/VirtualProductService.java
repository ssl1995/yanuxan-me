package com.msb.mall.product.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.product.mapper.VirtualProductMapper;
import com.msb.mall.product.model.entity.VirtualProduct;
import com.msb.mall.product.model.dto.admin.VirtualProductModifyDTO;
import com.msb.mall.product.model.vo.admin.VirtualProductVO;
import com.msb.mall.product.service.convert.VirtualProductConvert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 虚拟商品表(VirtualProduct)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-05-25 10:13:39
 */
@Service("virtualProductService")
public class VirtualProductService extends ServiceImpl<VirtualProductMapper, VirtualProduct> {

    @Resource
    private VirtualProductConvert virtualProductConvert;
    @Resource
    private VirtualProductMapper virtualProductMapper;

    /**
     * 根据商品id查询虚拟发货信息
     * @param productId 商品id
     * @return 虚拟商品VO集合
     */
    public List<VirtualProductVO> listVirtualProduct(Long productId) {
        return virtualProductConvert.toVo(this.lambdaQuery().eq(Objects.nonNull(productId), VirtualProduct::getProductId, productId).list());
    }

    /**
     * 批量删除并新增虚拟商品
     * @param productId 商品id
     * @param virtualProductModifyDTOList 虚拟商品集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllAndAdd(Long productId, List<VirtualProductModifyDTO> virtualProductModifyDTOList) {
        // 通过product_id将已有的虚拟商品信息删除
        virtualProductMapper.deleteByProductId(productId);
        List<VirtualProduct> virtualProducts = virtualProductConvert.toEntity(virtualProductModifyDTOList);
        virtualProducts.stream().forEach(item -> item.setProductId(productId));

        this.saveBatch(virtualProducts);
    }

}

