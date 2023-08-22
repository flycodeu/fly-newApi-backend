package com.flySdk.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class KuaiShouReSou implements Serializable {
    // 标题
    private String title;
    // 热度
    private String hot;
    // 地址
    private String url;
    // 更新时间
    private String updateTime;
}
