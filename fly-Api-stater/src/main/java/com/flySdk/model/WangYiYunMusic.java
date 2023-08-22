package com.flySdk.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class WangYiYunMusic implements Serializable {
    // 歌曲名字
    private String name;
    // 歌曲地址
    private String url;
    // 歌曲照片url
    private String pictureUrl;
    private static final long serialVersionUID = -1780708810726095685L;
}
