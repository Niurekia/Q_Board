package com.ComMangShop.Q_Board.domain.repository;

import com.ComMangShop.Q_Board.domain.entity.Thumb_up;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Thumb_upRepository extends JpaRepository<Thumb_up,Long> {
}
