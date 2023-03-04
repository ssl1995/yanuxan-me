package com.msb.sensitive.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.sensitive.model.dto.SensitiveWordsModifyDTO;
import com.msb.sensitive.model.entity.SensitiveWords;
import com.msb.sensitive.model.vo.SensitiveWordsVO;
import com.msb.sensitive.model.dto.SensitiveWordsDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 敏感词库(SensitiveWords)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-15 15:14:11
 */
@Mapper(componentModel = "spring")
public interface SensitiveWordsConvert {

    SensitiveWordsVO toVo(SensitiveWords sensitiveWords);

    List<SensitiveWordsVO> toVo(List<SensitiveWords> sensitiveWords);

    Page<SensitiveWordsVO> toVo(Page<SensitiveWords> sensitiveWords);

    SensitiveWords toEntity(SensitiveWordsDTO sensitiveWordsDTO);

    SensitiveWords toEntity(SensitiveWordsModifyDTO sensitiveWordsModifyDTO);
}

