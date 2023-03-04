package com.msb.oss.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.oss.mapper.OssFileRecordMapper;
import com.msb.oss.model.entity.OssFileRecord;
import com.msb.oss.model.vo.OssFileRecordVO;
import com.msb.oss.model.dto.OssFileRecordDTO;
import com.msb.oss.service.convert.OssFileRecordConvert;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.framework.common.model.PageDTO;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * (OssFileRecord)表服务实现类
 *
 * @author makejava
 * @date 2022-03-30 10:48:38
 */
@Service("ossFileRecordService")
public class OssFileRecordService extends ServiceImpl<OssFileRecordMapper, OssFileRecord> {

    @Resource
    private OssFileRecordConvert ossFileRecordConvert;

    public IPage<OssFileRecordVO> page(PageDTO pageDTO, OssFileRecordDTO ossFileRecordDTO) {
        return ossFileRecordConvert.toVo(this.page(pageDTO.page(), new QueryWrapper<OssFileRecord>().setEntity(ossFileRecordConvert.toDo(ossFileRecordDTO))));
    }

    public OssFileRecordVO getOne(Serializable id) {
        return ossFileRecordConvert.toVo(this.getById(id));
    }

    public Boolean save(OssFileRecordDTO ossFileRecordDTO) {
        return this.save(ossFileRecordConvert.toDo(ossFileRecordDTO));
    }

    public Boolean update(OssFileRecordDTO ossFileRecordDTO) {
        return this.updateById(ossFileRecordConvert.toDo(ossFileRecordDTO));
    }

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }
}

