package com.example.springboard.vo;


import lombok.Data;

@Data
public class CommentVO {
    private int commentNo;

    private int parentNo=0; //부모댓글 번호           ,부모가 없으면 0
    private int grp=0;      //그룹 루트(lv1) 댓글 번호 ,부모가 없으면 commentNo
    private int lv=0;       //레벨
    private int seq=0;      //그룹 내부 댓글 순서
    private int articleNo;  //게시글 번호

    private int userNo;     //댓글 작성자
    private String contents;//댓글 내용

    private String insDate;
    private String upDate;
}