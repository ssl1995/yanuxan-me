package com.msb.mall.product.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.product.mapper.ProductAttributeMapper;
import com.msb.mall.product.model.dto.admin.ProductAttributeDTO;
import com.msb.mall.product.model.dto.admin.SortModifyDTO;
import com.msb.mall.product.model.entity.ProductAttribute;
import com.msb.mall.product.model.vo.admin.ProductAttributeVO;
import com.msb.mall.product.model.vo.app.ProductAttributeSimpleVO;
import com.msb.mall.product.service.convert.ProductAttributeConvert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品属性(ProductAttribute)表服务实现类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Service
public class ProductAttributeService extends ServiceImpl<ProductAttributeMapper, ProductAttribute> {

    @Resource
    private ProductAttributeConvert productAttributeConvert;

    @Resource
    private ProductSkuService productSkuService;

    @Transactional(rollbackFor = Exception.class)
    public Long save(ProductAttributeDTO productAttributeDTO) {
        ProductAttribute productAttribute = productAttributeConvert.toEntity(productAttributeDTO);
        save(productAttribute);
        // 设置排序值
        getBaseMapper().setSort(productAttribute.getProductAttributeGroupId(), productAttribute.getId());
        // 设置symbol
        getBaseMapper().setSymbol(productAttribute.getProductId(), productAttribute.getId());
        return productAttribute.getId();
    }

    public Boolean update(Long attributeId, ProductAttributeDTO productAttributeDTO) {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setId(attributeId);
        productAttribute.setName(productAttributeDTO.getName());
        return updateById(productAttribute);
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

    /**
     * 查询属性组下的所有属性
     *
     * @param groupId 商品属性组id
     */
    public List<ProductAttributeVO> listVo(Long groupId) {
        return productAttributeConvert.toVo(list(groupId));
    }

    /**
     * 查询属性组下的所有属性
     *
     * @param groupId 商品属性组id
     */
    public List<ProductAttributeSimpleVO> listSimpleVo(Long groupId) {
        return productAttributeConvert.toSimpleVo(list(groupId));
    }

    /**
     * 查询属性组下的所有属性，排序
     */
    private List<ProductAttribute> list(Long groupId) {
        return lambdaQuery().eq(ProductAttribute::getProductAttributeGroupId, groupId)
                .orderByAsc(ProductAttribute::getSort)
                .list();
    }

    /**
     * 更新排序
     *
     * @param sortModifyDTO 排序信息
     */
    public Boolean updateSort(SortModifyDTO sortModifyDTO) {
        Long attributeId = sortModifyDTO.getId();
        Integer oldSort = sortModifyDTO.getOldSort();
        Integer currentSort = sortModifyDTO.getCurrentSort();
        ProductAttribute productAttribute = lambdaQuery()
                .select(ProductAttribute::getProductId)
                .eq(ProductAttribute::getId, attributeId).oneOpt()
                .orElseThrow(() -> new RuntimeException("找不到该属性id:" + attributeId));
        Long productId = productAttribute.getProductId();
        return getBaseMapper().updateSort(productId, attributeId, oldSort, currentSort);
    }
}

