package com.ComMangShop.Q_Board.domain.repository;

import com.ComMangShop.Q_Board.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {


    @Query("SELECT r FROM Reply r WHERE bno = :bno ORDER BY rno DESC")
    List<Reply> GetReplyByBnoDesc(@Param("bno") Long bno);

    @Query("SELECT COUNT(r) FROM Reply r WHERE bno = :bno")
    Long GetReplyCountByBnoDesc(@Param("bno") Long bno);


    @Query("UPDATE Reply r SET r.content=:content, r.regdate=:regdate WHERE r.rno = :rno")
    Integer updateReply(
            @Param("content") String content,
            @Param("regdate") String regdate

    );

}
