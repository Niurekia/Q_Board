package com.ComMangShop.Q_Board.restcontroller;


import com.ComMangShop.Q_Board.domain.dto.ReplyDto;
import com.ComMangShop.Q_Board.domain.entity.User;
import com.ComMangShop.Q_Board.domain.repository.UserRepository;
import com.ComMangShop.Q_Board.domain.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardRestController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserRepository userRepository;




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

    @PostMapping("/reply/update")
    public ResponseEntity<String> updateReply(@RequestBody ReplyDto replyDto) {
        try {
            // 수정된 내용을 데이터베이스에 업데이트
            boardService.updateReply(replyDto);
            return ResponseEntity.ok("댓글 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 수정 실패");
        }
    }

    //-------------------
    //권한
    //-------------------
    @GetMapping("/getRole/{username}")
    public String getRole(@PathVariable String username) {

            User user = userRepository.findById(username).get();
            String role = user.getRole();
            return role;
    }

    






}
