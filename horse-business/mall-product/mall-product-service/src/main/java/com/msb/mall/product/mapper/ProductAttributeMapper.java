package com.msb.mall.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.product.model.entity.ProductAttribute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品属性(ProductAttribute)表数据库访问层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
public interface ProductAttributeMapper extends BaseMapper<ProductAttribute> {

    /**
     * 设置排序
     * 取属性组id中的排序最大值+1
     *
     * @param attributeGroupId 属性组id
     * @param attributeId      属性id
     */
    @Update("update product_attribute a,(select ifnull(max(b.sort), 0) max_sort from product_attribute b where product_attribute_group_id = ${attributeGroupId}) temp set a.sort = temp.max_sort+1 where id = ${id}")
    void setSort(@Param("attributeGroupId") Long attributeGroupId, @Param("id") Long attributeId);

    /**
     * 设置symbol
     * 取该商品id中的排序最大值+1
     *
     * @param productId   商品id
     * @param attributeId 属性id
     */
    @Update("update product_attribute a,(select ifnull(max(b.symbol), 0) max_symbol from product_attribute b where product_id = ${productId}) temp set a.symbol = temp.max_symbol+1 where id = ${id}")
    void setSymbol(@Param("productId") Long productId, @Param("id") Long attributeId);

    /**
     * 更新排序
     * <p>
     * 如果位置往后调，则新老位置之间的元素的sort全部减1
     * 反之如果位置往前调，则新老位置之间的元素的sort全部加1
     * 最后将该元素的id的排序值设置成新排序
     *
     * @param productId               商品id
     * @param productAttributeGroupId 商品属性组id
     * @param oldSort                 旧排序
     * @param newSort                 新排序
     * @return boolean
     */
    @Update({
            "update product_attribute set sort = if(${oldSort}>${newSort}, sort+1, sort-1) where product_id = ${productId} and (sort between ${oldSort} and ${newSort} or sort between ${newSort} and ${oldSort});",
            "update product_attribute set sort = ${newSort} where id = ${productAttributeGroupId}"
    })
    boolean updateSort(@Param("productId") Long productId, @Param("productAttributeGroupId") Long productAttributeGroupId, @Param("oldSort") int oldSort, @Param("newSort") int newSort);

}

