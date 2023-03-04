package com.msb.search.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 搜索配置(SearchConfig)表实体类
 *
 * @author luozhan
 * @since 2022-06-10 15:14:05
 */
@Data
public class SearchConfigVO implements Serializable {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("搜索码，同一系统下唯一")
    private String searchCode;

    @ApiModelProperty("搜索名（描述）")
    private String searchName;

    @ApiModelProperty("搜索字段配置（格式“字段名/搜索类型”，搜索类型即ES中字段的分词器类型，模糊搜索standard，拼音搜索pinyin，全词匹配搜索keyword)")
    private String searchFiled;

    @ApiModelProperty("数据库名称")
    private String databaseName;

    @ApiModelProperty("documentId表达式（数据导入ES后，作为ES文档id的字段，使用占位符形式，如:${offer_id}+${offer_sub_id}（连表查询一对多时要注意是多个表的主键连接才能确定唯一性）)")
    private String documentIdExp;

    @ApiModelProperty("数据同步cron表达式（示例：‘* */30 * * * ?’（每30分钟同步一次））")
    private String syncCron;

    @ApiModelProperty("作者")
    private String author;

}

