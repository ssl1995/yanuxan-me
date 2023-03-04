package com.msb.sensitive.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.web.result.BizAssert;
import com.msb.sensitive.api.model.SensitiveWordsDO;
import com.msb.sensitive.mapper.SensitiveWordsMapper;
import com.msb.sensitive.model.dto.SensitiveWordsModifyDTO;
import com.msb.sensitive.model.dto.SensitiveWordsQueryDTO;
import com.msb.sensitive.model.entity.SensitiveWords;
import com.msb.sensitive.model.vo.SensitiveWordsVO;
import com.msb.sensitive.service.convert.SensitiveWordsConvert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


/**
 * 敏感词库(SensitiveWords)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-15 15:14:10
 */
@Service("sensitiveWordsService")
public class SensitiveWordsService extends ServiceImpl<SensitiveWordsMapper, SensitiveWords> {

    @Resource
    private SensitiveWordsConvert sensitiveWordsConvert;
    @Resource
    private SensitiveWordsUtilService sensitiveWordsUtilService;
    @Resource
    private SplitWordService splitWordService;

    /**
     * 先分词，再检查是否包含敏感词，如果包含则替换敏感词
     * @param content 内容
     * @return 替换敏感词后的内容
     */
    public SensitiveWordsDO checkIfContainSensitive(String content) {
        BizAssert.notNull(content, "内容为空");
        List<String> stringList = splitWordService.wordAnalyzer(content, true);
        // 默认不包含敏感词
        boolean containFlag = false;
        if (CollectionUtils.isEmpty(stringList)) {
            // 分词结果为空则使用原来的内容进行敏感词替换
            containFlag = sensitiveWordsUtilService.contains(content);
            String contentOfReplace = sensitiveWordsUtilService.replaceSensitiveWord(content);
            return new SensitiveWordsDO().setIsContainSensitiveWords(containFlag).setContent(contentOfReplace);
        }
        // 对分词后的词语进行敏感词替换再拼接返回
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : stringList) {
            boolean contains = sensitiveWordsUtilService.contains(string);
            if (contains) {
                containFlag = true;
            }
            String s = sensitiveWordsUtilService.replaceSensitiveWord(string);
            stringBuilder.append(s);
        }
        return new SensitiveWordsDO().setIsContainSensitiveWords(containFlag).setContent(stringBuilder.toString());
    }

    /**
     * 保存敏感词
     * @param sensitiveWordsModifyDTO 敏感词dto
     * @return boolean
     */
    public Boolean save(SensitiveWordsModifyDTO sensitiveWordsModifyDTO) {
        return this.save(sensitiveWordsConvert.toEntity(sensitiveWordsModifyDTO));
    }

    /**
     * 分页查询敏感词
     * @param sensitiveWordsQueryDTO 查询dto
     * @return 敏感词
     */
    public IPage<SensitiveWordsVO> page(SensitiveWordsQueryDTO sensitiveWordsQueryDTO) {
        Page<SensitiveWords> page = this.lambdaQuery()
                .eq(Objects.nonNull(sensitiveWordsQueryDTO.getWordsType()), SensitiveWords::getWordsType, sensitiveWordsQueryDTO.getWordsType())
                .eq(Objects.nonNull(sensitiveWordsQueryDTO.getEnabled()), SensitiveWords::getEnabled, sensitiveWordsQueryDTO.getEnabled())
                .like(Objects.nonNull(sensitiveWordsQueryDTO.getContent()), SensitiveWords::getContent, sensitiveWordsQueryDTO.getContent())
                .page(sensitiveWordsQueryDTO.page());
        return sensitiveWordsConvert.toVo(page);
    }

    /**
     * 更新敏感词
     * @param sensitiveWordsModifyDTO dyo
     * @return boolean
     */
    public Boolean update(SensitiveWordsModifyDTO sensitiveWordsModifyDTO) {
        return this.updateById(sensitiveWordsConvert.toEntity(sensitiveWordsModifyDTO));
    }
}

