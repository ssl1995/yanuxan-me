package com.msb.mall.product.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.product.mapper.ProductAttributeGroupMapper;
import com.msb.mall.product.model.dto.admin.ProductAttributeGroupDTO;
import com.msb.mall.product.model.dto.admin.SortModifyDTO;
import com.msb.mall.product.model.entity.ProductAttributeGroup;
import com.msb.mall.product.model.vo.admin.ProductAttributeGroupVO;
import com.msb.mall.product.model.vo.app.ProductAttributeGroupSimpleVO;
import com.msb.mall.product.service.convert.ProductAttributeGroupConvert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品属性组(ProductAttributeGroup)表服务实现类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Service("productAttributeGroupService")
public class ProductAttributeGroupService extends ServiceImpl<ProductAttributeGroupMapper, ProductAttributeGroup> {

    @Resource
    private ProductAttributeGroupConvert productAttributeGroupConvert;


    @Transactional(rollbackFor = Exception.class)
    public Long save(ProductAttributeGroupDTO productAttributeGroupDTO) {
        ProductAttributeGroup productAttributeGroup = productAttributeGroupConvert.toEntity(productAttributeGroupDTO);
        // 保存并设置排序值
        save(productAttributeGroup);
        getBaseMapper().setSort(productAttributeGroup.getProductId(), productAttributeGroup.getId());
        return productAttributeGroup.getId();
    }

    public Boolean update(Long id, ProductAttributeGroupDTO productAttributeGroupDTO) {
        ProductAttributeGroup productAttributeGroup = productAttributeGroupConvert.toEntity(productAttributeGroupDTO);
        productAttributeGroup.setId(id);
        return updateById(productAttributeGroup);
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }


    /**
     * 根据商品id获取属性组
     */
    public List<ProductAttributeGroupSimpleVO> getSimpleVOByProductId(Long productId) {
        return productAttributeGroupConvert.toSimpleVo(getByProductId(productId));
    }

    /**
     * 根据商品id获取属性组
     */
    public List<ProductAttributeGroupVO> getVOByProductId(Long productId) {
        return productAttributeGroupConvert.toVo(getByProductId(productId));
    }

    private List<ProductAttributeGroup> getByProductId(Long productId) {
        return lambdaQuery().eq(ProductAttributeGroup::getProductId, productId)
                .orderByAsc(ProductAttributeGroup::getSort)
                .list();
    }

    /**
     * 更新排序
     *
     * @param sortModifyDTO 排序信息
     */
    public Boolean updateSort(SortModifyDTO sortModifyDTO) {
        Long groupId = sortModifyDTO.getId();
        Integer oldSort = sortModifyDTO.getOldSort();
        Integer currentSort = sortModifyDTO.getCurrentSort();
        ProductAttributeGroup productAttributeGroup = lambdaQuery()
                .select(ProductAttributeGroup::getProductId)
                .eq(ProductAttributeGroup::getId, groupId).oneOpt()
                .orElseThrow(() -> new RuntimeException("找不到该属性组id:" + groupId));
        Long productId = productAttributeGroup.getProductId();
        // 更新sku列表信息
        // todo
        return getBaseMapper().updateSort(productId, groupId, oldSort, currentSort);
    }
}

