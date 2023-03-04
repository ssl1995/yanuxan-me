package com.msb.user.core.service;

import java.time.LocalDateTime;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.net.HttpClient;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.redis.annotation.CacheExpire;
import com.msb.framework.web.result.BizAssert;
import com.msb.user.core.mapper.PermissionMapper;
import com.msb.user.core.model.dto.PermissionDTO;
import com.msb.user.core.model.entity.Permission;
import com.msb.user.core.model.vo.PermissionVO;
import com.msb.user.core.model.vo.RolePermissionRelationVO;
import com.msb.user.core.service.convert.PermissionConvert;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.msb.user.core.model.constant.RedisKeysConstants.GET_EMPLOYEE_USERID;
import static com.msb.user.core.model.constant.RedisKeysConstants.GET_PERMISSION;

/**
 * 权限表(Permission)表服务实现类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Service("permissionService")
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    @Resource
    private PermissionConvert permissionConvert;

    public List<PermissionVO> listNotDistributionPermission() {
        List<Permission> list = this.lambdaQuery().eq(Permission::getType, 1).eq(Permission::getMenuId, 0).list();
        return permissionConvert.toVo(list);
    }

    public List<Permission> listPermission() {
        return this.lambdaQuery().eq(Permission::getType, 2).eq(Permission::getIsEnable, Boolean.TRUE).list();
    }

    public List<Permission> listPermission(List<Long> permissionIdList) {
        if (permissionIdList.isEmpty()) {
            return Collections.emptyList();
        }
        return this.lambdaQuery().in(Permission::getId, permissionIdList).eq(Permission::getIsEnable, Boolean.TRUE).list();
    }

    public List<PermissionVO> listPermission(Long menuId) {
        List<Permission> list = this.lambdaQuery().eq(Permission::getMenuId, menuId).list();
        return permissionConvert.toVo(list);
    }

    public List<RolePermissionRelationVO> listRolePermissionRelation(Long menuId) {
        List<Permission> list = this.lambdaQuery().eq(Permission::getMenuId, menuId).list();
        return permissionConvert.toRolePermissionRelationVO(list);
    }

    private Permission getPermission(PermissionDTO permissionDTO) {
        return this.getPermission(permissionDTO.getUri(), permissionDTO.getMethod());
    }

    @CacheExpire(value = 3600 * 24)
    @Cacheable(value = GET_PERMISSION, key = "#method+':'+#uri")
    public Permission getPermission(String uri, String method) {
        return this.lambdaQuery().eq(Permission::getUri, uri).eq(Permission::getMethod, method).one();
    }

    public Boolean save(PermissionDTO permissionDTO) {
        BizAssert.isNull(getPermission(permissionDTO), "当前接口已经存在了");
        return this.save(permissionConvert.toEntity(permissionDTO));
    }

    public Boolean update(Long id, PermissionDTO permissionDTO) {
        Permission permission = getPermission(permissionDTO);
        BizAssert.isTrue(id.equals(permission.getId()), "当前接口已经存在了");
        return this.updateById(permissionConvert.toEntity(permissionDTO).setId(id));
    }

    public Boolean distributionPermissionMenu(Long permissionId, Long menuId) {
        Permission permission = new Permission().setId(permissionId).setMenuId(menuId);
        return this.updateById(permission);
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }


    public void savePermissionBatch(List<PermissionDTO> permissionListDTO) {
        List<Permission> permissionList = permissionConvert.toEntity(permissionListDTO);
        this.saveBatch(permissionList);
    }

    public List<PermissionVO> scanningSwaggerDoc(String swaggerUrl, String filterKeyword, Long systemId) {
        ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(swaggerUrl, String.class);
        String json = responseEntity.getBody();
        String service = JSON.parseObject(json).getJSONObject("info").getString("title");
        List<Permission> permissionList = parsingSwaggerApiDoc(service, systemId, filterKeyword, json);
        return permissionConvert.toVo(permissionList);
    }

    public void saveSwaggerApiDoc(String service, Long systemId, String filterKeyword, String json) {
        List<Permission> permissionList = parsingSwaggerApiDoc(service, systemId, filterKeyword, json);
        this.saveBatch(permissionList);
    }

    public List<Permission> parsingSwaggerApiDoc(String service, Long systemId, String filterKeyword, String json) {
        List<Permission> list = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject paths = jsonObject.getJSONObject("paths");
        Set<String> pathSet = paths.keySet();
        for (String path : pathSet) {
            JSONObject methodObject = paths.getJSONObject(path);
            Set<String> methodSet = methodObject.keySet();
            for (String method : methodSet) {
                String tags = methodObject.getJSONObject(method).getJSONArray("tags").getString(0);
                if (!tags.contains(filterKeyword)) {
                    break;
                }
                String summary = methodObject.getJSONObject(method).getString("summary");
                Permission permission = new Permission();
                permission.setSystemId(systemId);
                permission.setName(summary);
                permission.setService(service);
                permission.setUri(path);
                permission.setMenuId(0L);
                permission.setMethod(method);
                permission.setIsEnable(true);
                //导入的权限默认需要分配
                permission.setType(1);
                list.add(permission);
            }
        }
        return list.stream().filter(permission -> !lambdaQuery()
                .eq(Permission::getUri, permission.getUri())
                .eq(Permission::getMethod, permission.getMethod())
                .oneOpt().isPresent()).collect(Collectors.toList());

    }
}

