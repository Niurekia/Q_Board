package com.ComMangShop.Q_Board.restcontroller;


import com.ComMangShop.Q_Board.domain.dto.ReplyDto;
import com.ComMangShop.Q_Board.domain.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardRestController {

    @Autowired
    private BoardService boardService;




    //-------------------
    // 삭제하기
    //-------------------
    @DeleteMapping("/delete")
    public String delete(Long no){
        log.info("DELETE /board/delete no " + no);

        boolean isremoved =  boardService.removeBoard(no);
        if(isremoved)
            return "success";
        else
            return "failed";

    }

    //-------------------
    //댓글추가
    //-------------------
    @GetMapping("/reply/add")
    public void addReply(Long bno,String contents , String username){
        log.info("GET /board/reply/add " + bno + " " + contents + " " + username);
        boardService.addReply(bno,contents, username);
    }


    //-------------------
    //댓글 조회
    //-------------------
    @GetMapping("/reply/list")
    public List<ReplyDto> getListReply(Long bno){
        log.info("GET /board/reply/list " + bno);
        List<ReplyDto> list =  boardService.getReplyList(bno);
        return list;
    }




    //-------------------
    //댓글 카운트
    //-------------------
    @GetMapping("/reply/count")
    public Long getCount(Long bno){
        log.info("GET /board/reply/count " + bno);
        Long cnt = boardService.getReplyCount(bno);

        return cnt;
    }


    //-------------------
    //댓글수정
    //-------------------
    @GetMapping("/reply/update/{rno}")
    public void replyUpdate(Long rno, String content){
        log.info("GET /board/reply/update rno " + rno);

        boardService.updateReply(rno,content);

//        Reply reply= boardService.updateReply();
//
//        ReplyDto dto= new ReplyDto();
//        dto.setRno(reply.getRno());
//        dto.setBno(bno);
//        dto.setContent(reply.getContent());
//        dto.setRegdate(reply.getRegdate());
//        dto.setUsername(reply.getUsername());
//        dto.setLikecount(reply.getLikecount());
//        dto.setUnlikecount(reply.getUnlikecount());
//
//
//        model.addAttribute("replyDto",dto);


    }



    






}
