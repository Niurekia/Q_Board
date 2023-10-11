package com.ComMangShop.Q_Board.domain.repository;


import com.ComMangShop.Q_Board.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
}
