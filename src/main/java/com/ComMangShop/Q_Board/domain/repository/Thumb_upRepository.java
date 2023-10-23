package com.ComMangShop.Q_Board.domain.repository;

import com.ComMangShop.Q_Board.domain.entity.Thumb_up;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface Thumb_upRepository extends JpaRepository<Thumb_up,Long> {



    @Query("SELECT t FROM Thumb_up t where t.reply.rno=:rno AND t.user.username=:username")
    Thumb_up find_rno_username(@Param("rno") Long rno,@Param("username") String username);
}
