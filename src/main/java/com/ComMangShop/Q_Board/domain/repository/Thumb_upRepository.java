package com.ComMangShop.Q_Board.domain.repository;

import com.ComMangShop.Q_Board.domain.entity.Thumb_up;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Thumb_upRepository extends JpaRepository<Thumb_up,Long> {



    @Query("SELECT t FROM Thumb_up t where t.rno=:rno AND t.username=:username")
    Thumb_up find_rno_username(Long rno, String username);
}
