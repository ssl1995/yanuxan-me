package com.msb.mall.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.product.model.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品分类(ProductCategory)表数据库访问层
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    /**
     * 更新排序
     * <p>
     * 如果位置往后调，则新老位置之间的元素的sort全部减1
     * 反之如果位置往前调，则新老位置之间的元素的sort全部加1
     *
     * @param id      商品分类id
     * @param oldSort 旧排序
     * @param newSort 新排序
     * @return 更新数量
     */
    @Update({
            "update product_category set sort = if(${oldSort}>${newSort}, sort+1, sort-1) where (sort between ${oldSort} and ${newSort} or sort between ${newSort} and ${oldSort});",
            "update product_category set sort = ${newSort} where id = ${id}"
    })
    boolean updateSort(@Param("id") Long id, @Param("oldSort") int oldSort, @Param("newSort") int newSort);


    /**
     * 设置排序
     * 取同级分类中排序最大值
     *
     * @param parentId 父id
     * @param id       分类id
     * @return boolean
     */
    @Update("update product_category a,(select ifnull(max(b.sort), 0) max_sort from product_category b where parent_id = ${parentId}) temp set a.sort = temp.max_sort+1 where id = ${id}")
    boolean setSort(@Param("parentId") Long parentId, @Param("id") Long id);
}

