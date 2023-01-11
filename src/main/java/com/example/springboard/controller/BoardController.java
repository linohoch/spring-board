package com.example.springboard.controller;

import com.example.springboard.service.BoardService;
import com.example.springboard.service.S3Service;
import com.example.springboard.util.CookieUtil;
import com.example.springboard.vo.ArticleVO;
import com.example.springboard.vo.CommentVO;
import com.example.springboard.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final S3Service s3Service;

    @GetMapping("/test")
    public ResponseEntity<?> test2(@RequestParam("articleNo") int articleNo,
                                   @RequestParam("userNo") int userNo) {
        log.info("//{}", articleNo);
        Object result = boardService.procTest2(articleNo, userNo);
        log.info("result //{}", result);
        return ResponseEntity.ok().body(result);
    }
    @Operation(method = "POST", description = "게시글 추가")
    @PostMapping("/article")
    public ResponseEntity<?> postArticle(
            @RequestPart(value="files", required = false) List<MultipartFile> multipartFileList,
            @RequestBody ArticleVO articleVO){
        log.info("articleVO//{}",articleVO);
        Optional<List<MultipartFile>> listCheck = Optional.ofNullable(multipartFileList);
        try{
            if(listCheck.isPresent()){
                List<Map<String,Object>> fileList = s3Service.upload(multipartFileList);
                int result = boardService.articlePhotoIns(articleVO, fileList);
                log.info("photoins//{}", result);
            }else{
                if(boardService.articleIns(articleVO)==0){
                    return ResponseEntity.status(204).body("incomplete");
                }
            }
        }catch (Exception e){
            log.error("board ins ex//{}",e.toString());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body("ins complete");
    }
    @Operation(method = "GET", description = "게시글 목록 조회")
    @GetMapping("/{page}")
    public ResponseEntity<?> loadPage(@PathVariable("page") int pageNo,
                                      @RequestParam("per-page") int cntPerPage) {
        try {
            List<ArticleVO> list = boardService.articleListSel(pageNo, cntPerPage);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            log.error("boardCon//{}",e.toString());
            return ResponseEntity.status(404).build();
        }
    }
    @Operation(method = "GET", description = "게시글 상세 조회")
    @GetMapping("/article/{articleNo}")
    public ResponseEntity<?> loadArticle(@PathVariable("articleNo") int articleNo,
                                         @RequestBody UserInfoVO userInfoVO){
        List<Object> article = boardService.articleDetailSel(articleNo, userInfoVO.getUserNo());
        List<Object> comment = boardService.commentListSel(articleNo, 1, 50);
        Map<String, Object> result = new HashMap<>();
        result.put("article", article);
        result.put("comment", comment);
        return ResponseEntity.ok().body(result);
    }
    @Operation(method = "PUT", description = "게시글 수정")
    @PutMapping("/article/{articleNo}")
    public ResponseEntity<?> updateArticle(@PathVariable("articleNo") int articleNo,
                                           @RequestBody ArticleVO articleVO){
        return ResponseEntity.ok().body("");
    }
    @Operation(method = "GET", description = "게시글의 댓글목록 조회")
    @GetMapping("/article/{articleNo}/comment")
    public ResponseEntity<?> loardCommentList(@PathVariable("articleNo") int no){
        return ResponseEntity.ok().body("");
    }
    @Operation(method = "PUT", description = "게시글의 댓글 수정")
    @PutMapping("/article/{articleNo}/comment/{commentNo}")
    public ResponseEntity<?> updateComment(@PathVariable("articleNo") int articleNo,
                                           @PathVariable("commentNo") int commentNo,
                                           @RequestBody CommentVO commentVO){
        boardService.commentUpd(articleNo, commentNo, commentVO.getContents());
        return ResponseEntity.ok().body("");
    }
    @Operation(method = "POST", description = "게시글의 댓글 추가")
    @PostMapping("/article/{articleNo}/comment")
    public ResponseEntity<?> postComment(@PathVariable("articleNo") int articleNo,
                                         @RequestBody CommentVO commentVO){
        boardService.commentIns(commentVO);
        return ResponseEntity.ok().body("");
    }
    @Operation(method = "DELETE", description = "n번 게시물 m번 코멘트 삭제")
    @DeleteMapping("/article/{articleNo}/comment/{commentNo}")
    public ResponseEntity<?> deleteComment(@PathVariable("articleNo") int articleNo,
                                           @PathVariable("commentNo") int commentNo,
                                           @RequestBody UserInfoVO userInfoVO){
        boardService.commentDel(articleNo, commentNo);
        //TODO userNo 확인
        return null;
    }

    @Operation(method = "PUT", description = "n번 게시물 좋아요 추가")
    @PutMapping("article/{articleNo}/like")
    public ResponseEntity<?> putLike(@PathVariable("articleNo") int articleNo,
//                                     @PathVariable("userNo") int userNo
                                     HttpServletRequest request
    ){
        int userNo=Integer.parseInt(CookieUtil.getCookieValue(request,"userNo"));
        int result = boardService.articleLikeUp(articleNo, userNo);
        if(result>0) return ResponseEntity.status(201).build();
        return ResponseEntity.status(202).build();
    }
    @Operation(method = "DELETE", description = "n번 게시물 좋아요 삭제")
    @DeleteMapping("article/{articleNo}/like")
    public ResponseEntity<?> deleteLike(@PathVariable("articleNo") int articleNo,
//                                        @PathVariable("userNo") int userNo
                                        HttpServletRequest request
    ){
        int userNo=Integer.parseInt(CookieUtil.getCookieValue(request,"userNo"));
        int result = boardService.articleLikeDown(articleNo, userNo);
        if(result>0) return ResponseEntity.status(201).build();
        return ResponseEntity.status(202).build();
    }
}