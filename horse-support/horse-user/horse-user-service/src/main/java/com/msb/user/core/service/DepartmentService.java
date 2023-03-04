package com.msb.user.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.common.utils.TreeUtils;
import com.msb.framework.redis.annotation.CacheExpire;
import com.msb.user.core.mapper.DepartmentMapper;
import com.msb.user.core.model.constant.RedisKeysConstants;
import com.msb.user.core.model.dto.DepartmentDTO;
import com.msb.user.core.model.entity.Department;
import com.msb.user.core.model.vo.DepartmentTreeVO;
import com.msb.user.core.model.vo.DepartmentVO;
import com.msb.user.core.service.convert.DepartmentConvert;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门表(Department)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Service("departmentService")
public class DepartmentService extends ServiceImpl<DepartmentMapper, Department> {

    @Resource
    private DepartmentConvert departmentConvert;

    public List<DepartmentVO> listDepartmentVO(List<Long> idList) {
        if (idList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Department> list = this.lambdaQuery().in(Department::getId, idList).list();
        return departmentConvert.toVo(list);
    }

    public IPage<DepartmentVO> page(PageDTO pageDTO) {
        return departmentConvert.toVo(this.page(pageDTO.page()));
    }

    public List<DepartmentVO> getDepartmentChild(Long id) {
        List<Department> list = this.lambdaQuery().eq(Department::getParentId, id).list();
        return departmentConvert.toVo(list);
    }

    public List<DepartmentTreeVO> getDepartmentTreeVO(Boolean hiddenDisable) {
        List<Department> list = this.lambdaQuery().eq(hiddenDisable, Department::getIsEnable, Boolean.TRUE).list();
        List<DepartmentTreeVO> departmentTreeVO = departmentConvert.toTreeVo(list);
        Collection<DepartmentTreeVO> departmentTreeListVO = TreeUtils.toTree(departmentTreeVO, "id", "parentId", "childDepartment", DepartmentTreeVO.class);
        if (Objects.isNull(departmentTreeListVO)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(departmentTreeListVO);
    }

    public DepartmentVO getOne(Serializable id) {
        return departmentConvert.toVo(this.getById(id));
    }

    @CacheExpire(value = 3600)
    @Cacheable(value = RedisKeysConstants.GET_DEPARTMENT_CHAIN_NAME, key = "#departmentId")
    public String getDepartmentChainName(Long departmentId) {
        Department department = this.getById(departmentId);
        if (department == null) {
            return "";
        }
        String chainId = department.getChainId();
        String[] departmentIds = chainId.split("-");
        List<Department> list = this.lambdaQuery().in(Department::getId, Arrays.asList(departmentIds)).list();
        return list.stream().map(Department::getName).collect(Collectors.joining(">"));
    }

    @Transactional
    public Boolean save(DepartmentDTO departmentDTO) {
        this.lambdaQuery().eq(Department::getName, departmentDTO.getName())
                .list()
                .stream()
                .findAny()
                .ifPresent(department -> {
                    throw new BizException("部门已经存在");
                });

        Department department = departmentConvert.toDo(departmentDTO);
        this.save(department);
        if (Objects.isNull(department.getParentId()) || Objects.equals(department.getParentId(), 0L)) {
            department.setChainId(department.getId().toString());
        } else {
            Optional<Department> departmentOptional = this.lambdaQuery().eq(Department::getId, department.getParentId()).oneOpt();
            department.setChainId(departmentOptional
                    .map(Department::getChainId)
                    .orElseThrow(() -> new BizException("找不到上级部门"))
                    .concat("-")
                    .concat(department.getId().toString()));
        }
        this.updateById(department);
        return true;
    }

    public Boolean update(Long id, DepartmentDTO departmentDTO) {
        return this.updateById(departmentConvert.toDo(departmentDTO).setId(id));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

