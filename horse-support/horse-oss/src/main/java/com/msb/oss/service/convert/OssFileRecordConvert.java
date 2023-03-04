package com.msb.oss.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.oss.model.entity.OssFileRecord;
import com.msb.oss.model.vo.OssFileRecordVO;
import com.msb.oss.model.dto.OssFileRecordDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (OssFileRecord)表服务接口
 *
 * @author makejava
 * @date 2022-03-30 10:48:38
 */
@Mapper(componentModel = "spring")
public interface OssFileRecordConvert {

    OssFileRecordVO toVo(OssFileRecord ossFileRecord);

    List<OssFileRecordVO> toVo(List<OssFileRecord> ossFileRecord);

    Page<OssFileRecordVO> toVo(Page<OssFileRecord> ossFileRecord);

    OssFileRecord toDo(OssFileRecordDTO ossFileRecordDTO);
}

