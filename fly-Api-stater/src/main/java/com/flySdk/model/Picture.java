package com.flySdk.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Picture implements Serializable {
    private static final long serialVersionUID = 3946514657018193905L;
    private String title;
    private String url;


}
