package com.msb.sensitive.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.msb.sensitive.enums.EnableTypeEnum;
import com.msb.sensitive.mapper.SensitiveWordsMapper;
import com.msb.sensitive.model.entity.SensitiveWords;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 敏感词处理工具 - DFA算法实现
 *
 * @author shumengjiao
 */
@Service("sensitiveWordUtilService")
public class SensitiveWordsUtilService {

    @Resource
    private SensitiveWordsMapper sensitiveWordsMapper;

    /**
     * 敏感词匹配规则
     * 1-最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
     * 2-最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]
     */
    public static final int MIN_MATCH_YPE = 1;
    public static final int MAX_MATCH_TYPE = 2;

    /**
     * 敏感词集合
     */
    protected static HashMap<String, String> sensitiveWordMap;

    /**
     * 初始化敏感词库，构建DFA算法模型
     */
    @PostConstruct
    public void init() {
        // 查询敏感词库
        Wrapper<SensitiveWords> query = Wrappers.<SensitiveWords>lambdaQuery()
                .eq(SensitiveWords::getEnabled, EnableTypeEnum.ENABLE.getCode())
                .eq(SensitiveWords::getIsDeleted, false);
        List<SensitiveWords> sensitiveWords = sensitiveWordsMapper.selectList(query);
        Set<String> sensitiveWordSet = sensitiveWords.stream().map(SensitiveWords::getContent).collect(Collectors.toSet());
        // 初始化敏感词map
        initSensitiveWordMap(sensitiveWordSet);
    }

    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     * @param sensitiveWordSet 敏感词库
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        //初始化敏感词容器，减小扩容操做
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代sensitiveWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            //关键字
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //库中获取关键字
                Object wordMap = nowMap.get(keyChar);
                //若是存在该key，直接赋值，用于下一个循环获取
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，由于他不是最后一个
                    newWorMap = new HashMap<>();
                    //不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt       文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，不然返回false
     */
    public boolean contains(String txt, Integer matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int matchFlag = checkSensitiveWord(txt, i, matchType);
            //大于0存在，返回true
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txtList   文字集合
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，不然返回false
     */
    public boolean contains(List<String> txtList, Integer matchType) {
        // 默认为最小匹配规则
        matchType = matchType == null ? MIN_MATCH_YPE : matchType;
        boolean flag = false;
        for (String txt : txtList) {
            for (int i = 0; i < txt.length(); i++) {
                //判断是否包含敏感字符
                int matchFlag = checkSensitiveWord(txt, i, matchType);
                //大于0存在，返回true
                if (matchFlag > 0) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt 文字
     * @return 若包含返回true，不然返回false
     */
    public boolean contains(String txt) {
        return contains(txt, MIN_MATCH_YPE);
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return 敏感词
     */
    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = checkSensitiveWord(txt, i, matchType);
            //存在,加入list中
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                //减1的缘由，是由于for会自增
                i = i + length - 1;
            }
        }

        return sensitiveWordList;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt 文字
     * @return 敏感词
     */
    public Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MAX_MATCH_TYPE);
    }

    /**
     * 替换敏感字字符
     *
     * @param txt         文本
     * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @param matchType   敏感词匹配规则
     * @return 替换后的文本
     */
    public String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        //获取全部的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt         文本
     * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @return 替换后的文本
     */
    public String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MAX_MATCH_TYPE);
    }

    /**
     * 替换敏感字字符
     *
     * @param txt        文本
     * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
     * @param matchType  敏感词匹配规则
     * @return 替换后的文本
     */
    public String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        String resultTxt = txt;
        //获取全部的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            int length = word.length();
            String replaceString = getReplaceChars(replaceStr, length);
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt        文本
     * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
     * @return 替换后的文本
     */
    public String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, MAX_MATCH_TYPE);
    }

    /**
     * 替换敏感字字符
     *
     * @param txt 文本
     * @return 替换后的文本
     */
    public String replaceSensitiveWord(String txt) {
        return replaceSensitiveWord(txt, "*", MAX_MATCH_TYPE);
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar 替换的字符串
     * @param length      长度
     * @return String
     */
    private String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceString 替换的字符串
     * @param length        长度
     * @return String
     */
    private String getReplaceChars(String replaceString, int length) {
        String string = "";
        for (int i = 0; i < length; i++) {
            string = string + replaceString;
        }

        return string;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则以下
     *
     * @param txt 文本
     * @param beginIndex 开始下表
     * @param matchType 匹配类型
     * @return 若是存在，则返回敏感词字符的长度，不存在返回0
     */
    private int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        //敏感词结束标识位：用于敏感词只有1位的状况
        boolean flag = false;
        //匹配标识数默认为0
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            //获取指定key
            nowMap = (Map) nowMap.get(word);
            //存在，则判断是否为最后一个
            if (nowMap != null) {
                //找到相应key，匹配标识+1
                matchFlag++;
                //若是为最后一个匹配规则,结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    //结束标志位为true
                    flag = true;
                    //最小规则，直接返回,最大规则还需继续查找
                    if (MIN_MATCH_YPE == matchType) {
                        break;
                    }
                }
            } else {
                //不存在，直接返回
                break;
            }
        }
        //长度必须大于等于1，为词
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

}