package com.msb.sensitive.service;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shumengjiao
 */
@Slf4j
@Service("splitWordService")
public class SplitWordService {

    /**
     * 对字符串进行分词
     * @param content 分词内容
     * @param useSmart 是否使用智能分词 true-使用只能分词 false-使用最细粒度分词
     * @return 分词结果
     * @throws Exception
     */
    public List<String> wordAnalyzer(String content, boolean useSmart){
        StringReader sr = new StringReader(content);
        List<String> stringList = new ArrayList<>();
        IKSegmenter ikSegmenter = new IKSegmenter(sr, useSmart);
        Lexeme word = null;
        String w = null;
        try {
            while((word = ikSegmenter.next()) != null){
                w = word.getLexemeText();
                stringList.add(w);
            }
        } catch (Exception e) {
            log.info("分词失败");
        }
        return stringList;
    }
}
