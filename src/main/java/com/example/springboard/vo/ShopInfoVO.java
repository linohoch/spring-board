package com.example.springboard.vo;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ShopInfoVO {
    private int shopNo;
    private String shopName;
    private String shopDisc;
    private String shopLat;
    private String shopLong;
    private String rpstPhoto;
    private int star;
    private int userNo;
    private String insDate;
    private String upDate;
    private int chrgrNo;
}
