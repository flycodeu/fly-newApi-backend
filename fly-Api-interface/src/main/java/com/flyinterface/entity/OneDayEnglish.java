package com.flyinterface.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OneDayEnglish implements Serializable {
    // 引用地址
    private String url ;
    // 内容
    private String content;
    // 翻译
    private String note;
    // 更新时间
    private String dateline;
    // 图片地址
    private String img;
    private static final long serialVersionUID = 2357421840617006456L;
}
