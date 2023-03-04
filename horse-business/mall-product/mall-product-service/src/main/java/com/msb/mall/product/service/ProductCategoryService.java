package com.msb.mall.product.service;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.product.mapper.ProductCategoryMapper;
import com.msb.mall.product.model.dto.admin.ProductCategoryModifyDTO;
import com.msb.mall.product.model.dto.admin.SortModifyDTO;
import com.msb.mall.product.model.entity.ProductCategory;
import com.msb.mall.product.model.vo.admin.ProductCategoryVO;
import com.msb.mall.product.model.vo.app.ProductCategoryAppVO;
import com.msb.mall.product.model.vo.app.ProductSimpleVO;
import com.msb.mall.product.service.convert.ProductCategoryConvert;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 商品分类(ProductCategory)表服务实现类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Service("productCategoryService")
public class ProductCategoryService extends ServiceImpl<ProductCategoryMapper, ProductCategory> {

    @Resource
    private ProductService productService;
    @Resource
    private ProductCategoryConvert productCategoryConvert;
    @Resource
    private ProductCategoryMapper productCategoryMapper;

    private static final long PARENT_ID = 0L;

    /**
     * 查询一级分类
     *
     * @return list
     */
    public List<ProductCategoryAppVO> listLevelOne() {
        List<ProductCategory> list = appLambdaQuery().eq(ProductCategory::getLevel, 1).list();
        return productCategoryConvert.toAppVo(list);
    }

    /**
     * 查询所有并用树展示
     *
     * @return list
     */
    public List<ProductCategoryVO> listUseTree() {
        List<ProductCategory> list = lambdaQuery().orderByAsc(ProductCategory::getSort).list();
        List<ProductCategoryVO> productCategoryVOList = productCategoryConvert.toVo(list);

        return ListUtil.toTree(productCategoryVOList, PARENT_ID);
    }

    public ProductCategoryVO getOne(Serializable id) {
        return productCategoryConvert.toVo(this.getById(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean save(ProductCategoryModifyDTO productCategoryModifyDTO) {
        ProductCategory productCategory = productCategoryConvert.toEntity(productCategoryModifyDTO);
        Long parentId = productCategoryModifyDTO.getParentId();
        int level;
        // 一级分类的父id为0
        if (parentId == null || parentId == PARENT_ID) {
            level = 1;
        } else {
            ProductCategory parentProductCategory = getById(parentId);
            BizAssert.notNull(parentProductCategory, "parentId查询不到：" + parentId);
            level = parentProductCategory.getLevel() + 1;
        }
        productCategory.setLevel(level);

        save(productCategory);
        return getBaseMapper().setSort(parentId, productCategory.getId());
    }

    public Boolean update(Long id, ProductCategoryModifyDTO productCategoryModifyDTO) {
        ProductCategory productCategory = productCategoryConvert.toEntity(productCategoryModifyDTO);
        productCategory.setId(id);
        Long parentId = productCategory.getParentId();
        if (parentId != null && parentId != PARENT_ID) {
            // 设置分级：父分类分级+1
            ProductCategory parentCategory = getById(parentId);
            productCategory.setLevel(parentCategory.getLevel() + 1);
        }
        return updateById(productCategory);
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

    /**
     * 分类导航（banner下方区域）
     * 拿出一级分类（除开书籍）和书籍下的所有二级分类
     *
     * @return list
     */
    public List<ProductCategoryAppVO> listCategoryNavigation() {
        // 查询一级分类（除开书籍）
        int categoryIdOfBook = 6;
        List<ProductCategory> levelOneListWithoutBook = appLambdaQuery().eq(ProductCategory::getLevel, 1)
                .ne(ProductCategory::getId, categoryIdOfBook)
                .list();
        int size = levelOneListWithoutBook.size();
        // app上一共显示10个，一级分类不够使用书籍的二级分类补充
        int needSize = 10 - size;
        // 查询书籍下的所有二级分类
        List<ProductCategory> productCategoryListBelongsBook = appLambdaQuery().eq(ProductCategory::getParentId, categoryIdOfBook).last("limit " + needSize).list();
        List<ProductCategory> unionList = ListUtils.union(levelOneListWithoutBook, productCategoryListBelongsBook);
        return productCategoryConvert.toAppVo(unionList);
    }

    /**
     * app查询子级分类及商品
     */
    public List<ProductCategoryAppVO> listChildCategoryAndProduct(long categoryId) {
        List<ProductCategory> childCategoryList = appLambdaQuery()
                .eq(ProductCategory::getParentId, categoryId)
                .list();
        List<ProductCategoryAppVO> productCategoryAppVoList = productCategoryConvert.toAppVo(childCategoryList);
        productCategoryAppVoList.forEach(categoryVo -> {
            List<ProductSimpleVO> productList = productService.listSimpleProductByCategoryId(categoryVo.getId());
            categoryVo.setProductList(productList);
        });
        return productCategoryAppVoList;
    }

    /**
     * app查询默认设置：
     * 查询id、name、picture
     * isEnable=1
     * 按sort字段排序
     */
    public LambdaQueryChainWrapper<ProductCategory> appLambdaQuery() {
        return super.lambdaQuery().select(ProductCategory::getId, ProductCategory::getName, ProductCategory::getPicture, ProductCategory::getIcon)
                .eq(ProductCategory::getIsEnable, true)
                .orderByAsc(ProductCategory::getSort);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSort(SortModifyDTO sortModifyDTO) {
        Long id = sortModifyDTO.getId();
        Integer oldSort = sortModifyDTO.getOldSort();
        Integer currentSort = sortModifyDTO.getCurrentSort();
        return productCategoryMapper.updateSort(id, oldSort, currentSort);
    }

    /**
     * 转移分类下的商品
     *
     * @param oldId 老分类id
     * @param newId 新分类id
     * @return boolean
     */
    public int transfer(Long oldId, Long newId) {
        return productService.updateCategory(oldId, newId);
    }

    public List<ProductCategory> listByParentId(Long categoryId) {
        return lambdaQuery().eq(ProductCategory::getParentId, categoryId).list();
    }
}

