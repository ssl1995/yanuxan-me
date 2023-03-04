package com.msb.search.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 搜索配置(SearchConfig)表实体类
 *
 * @author luozhan
 * @since 2022-06-10 15:14:05
 */
@Data
@TableName("search_config")
public class SearchConfig implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 搜索码，同一系统下唯一
     */
    private String searchCode;

    /**
     * 搜索名（描述）
     */
    private String searchName;

    /**
     * 搜索字段配置，格式“字段名/搜索类型”，搜索类型即ES中字段的分词器类型，模糊搜索standard，拼音搜索pinyin，全词匹配搜索keyword
     */
    private String searchFiled;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 数据导入ES后，作为ES文档id的字段，使用占位符形式，如:${offer_id}+${offer_sub_id}（连表查询一对多时要注意是多个表的主键连接才能确定唯一性）
     */
    private String documentIdExp;

    /**
     * 同步数据到ES的频率（cron表达式）
     */
    private String syncCron;

    /**
     * 作者
     */
    private String author;

}

