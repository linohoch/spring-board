package com.example.springboard.proc;


import com.example.springboard.vo.ArticleVO;
import com.example.springboard.vo.CommentVO;
import com.example.springboard.vo.ShopInfoVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    //procTest
    @ResultMap({"ResultMap.ArticleVO"})
    @Select("CALL pj1.p_test_grant()")
    List<ArticleVO> testproc();
    @ResultMap({"ResultMap.ArticleVO","ResultMap.PhotoVO"})
    @Select("CALL pj1.p_article_detail_sel_v1(#{articleNo},#{userNo})")
    List<Object> testproc2(@Param("articleNo") int articleNo,
                           @Param("userNo") int userNo);

    //업체등록,수정,삭제
    @Select("CAll pj1.p_shopInfo_ins(#{shopName},#{shopDisc},#{shopLat},#{shopLong},#{rpstPhoto},#{userNo},#{chrgrNo})")
    int shopInfoIns(ShopInfoVO shopInfoVO);
    //업체리스트 가저오기
    @ResultMap("ResultMap.ShopInfoVO")
    @Select("CALl pj1.p_shopInfo_list_sel(#{pageNo},#{cntPerPage},#{orderCol},#{orderBy})")
    List<Object> shopInfoListSel_1(@Param("pageNo") int pageNo,
                                   @Param("cntPerPage") int cntPerPage,
                                   @Param("orderCol") char orderCol,
                                   @Param("orderBy") char orderBy);

    //업체디테일
    @ResultMap("ResultMap.ShopInfoVO")
    @Select("Select * from shopInfo where shop_name=#{shopName}")
    ShopInfoVO shopInfoSel(char op, String shopName);
//    int shopInfoUpd(ShopInfoVO shopInfoVO);
//    int shopInfoDel(ShopInfoVO shopInfoVO);

    //게시글리스트 가져오기
    @ResultMap({"ResultMap.integer","ResultMap.ArticleVO"})
    @Select("CALL pj1.p_article_list_sel(#{pageNo},#{cntPerPage},#{orderCol},#{orderBy})")
    List<Object> articleListSel(@Param("pageNo") int pageNo,
                                @Param("cntPerPage") int cntPerPage,
                                @Param("orderCol") char orderCol,
                                @Param("orderBy") char orderBy);
    //게시글 디테일 가져오기
    @ResultMap({"ResultMap.ArticleVO","ResultMap.PhotoVO"})
    @Select("CALL pj1.p_article_detail_sel_v2(#{articleNo}, #{userNo})")
    List<Object> articleDetailSel2(@Param("articleNo") int articleNo,
                                   @Param("userNo") int userNo);

    //조회수
    @Update("UPDATE pj1.article SET hit_cnt=hit_cnt+1 WHERE article_no=#{articleNo}")
    void articleHitUpd(@Param("articleNo") int articleNo);
    //좋아요
    //TODO proc으로 합쳐
    @Update("UPDATE pj1.article SET like_cnt=like_cnt+1 WHERE article_no=#{articleNo}")
    int articleLikeUp(@Param("articleNo") int articleNo);
    @Update("UPDATE pj1.article SET like_cnt=like_cnt-1 WHERE article_no=#{articleNo}")
    int articleLikeDown(@Param("articleNo") int articleNo);
    @Insert("Insert into pj1.user_article_likeYn(article_no, user_no, like_yn) VALUES(#{articleNo},#{userNo},'y')")
    int articleLikeUserIns(@Param("articleNo") int articleNo,
                           @Param("userNo") int userNo);
    @Delete("Delete FROM pj1.user_article_likeYn WHERE user_no=#{userNo} and article_no=#{articleNo}")
    int articleLikeUserDel(@Param("articleNo") int articleNo,
                           @Param("userNo") int userNo);

    //게시글등록
    @ResultMap("ResultMap.integer")
    @Select("CALL pj1.p_article_ins(#{shopNo},#{userNo},#{title},#{contents})")
    int articleIns(ArticleVO articleVO);
//    int articleIns1(ArticleVO articleVO);
    //첨부파일업로드
    int photoListIns(List<Map<String, Object>> imageInfoList);

    /**
     * method : 댓글등록
     * author : linohoch
     * description :
     * Comment ins int.
     *
     * @param commentVO the comment vo
     *                  commentNo   --부모댓글의 번호 -> 나의 parent_no
     *                  grp         --부모댓글의 그룹 루트(lv1) 댓글 번호
     *                  lv          --부모댓글의 레벨
     *                  seq         --부모댓글의 그룹내 댓글순서
     *                  articleNo   --게시글 번호
     *                  userNo      --댓글 작성자
     *                  contents    --댓글 내용
     * @return the int
     */
    @ResultMap("ResultMap.integer")
    @Select("CALL pj1.p_comment_ins_v1(#{commentNo},#{grp},#{lv},#{seq},#{articleNo},#{userNo},#{contents})")
    Integer commentIns(CommentVO commentVO);
    @Update("Update FROM pj1.comments(contents) VALUES(#{contents}) WHERE article_no=#{articleNo} and comment_no=#{commentNo}")
    int commentUpd(@Param("articleNo") int articleNo,
                   @Param("commentNo") int commentNo,
                   @Param("contents") String contents);
    @Delete("Delete FROM pj1.comments WHERE article_no=#{articleNo} and comment_no=#{commentNo}")
    int commentDel(@Param("articleNo") int articleNo,
                   @Param("commentNo") int commentNo);
    //댓글리스트 가져오기
    @ResultMap({"ResultMap.integer","ResultMap.CommentVO"})
    @Select("CALL pj1.p_comment_list_sel_v2(#{articleNo},#{pageNo},#{cntPerPage},#{orderSlct})")
    List<Object> commentListSel(@Param("articleNo") int articleNo,
                                @Param("pageNo") int pageNo,
                                @Param("cntPerPage") int cntPerPage,
                                @Param("orderSlct") char orderSlct);
    //내 게시물 가져오기
    List<ArticleVO> myArticleListSel(@Param("userNo") int userNo,
                                     @Param("pageNo") int pageNo,
                                     @Param("cntPerPage") int cntPerPage);
}
