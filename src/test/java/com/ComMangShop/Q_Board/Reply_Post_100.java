package com.ComMangShop.Q_Board;



import com.ComMangShop.Q_Board.domain.entity.Board;
import com.ComMangShop.Q_Board.domain.entity.Reply;
import com.ComMangShop.Q_Board.domain.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class Reply_Post_100 {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void t1()
    {
        Board board = new Board();
        board.setNo(1000L);
        for(Long i = 1L; i<=100; i++){
            Reply reply = new Reply();
            reply.setBoard(board);
            reply.setContent("REPLYREPLY~~" + i);
            reply.setUsername("user" + i+"@test.com");
            reply.setRegdate(LocalDateTime.now());
            reply.setLikecount(0L);
            reply.setUnlikecount(0L);

            replyRepository.save(reply);
        }


    }
}
