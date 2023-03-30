package com.project.learn.main.repository;

import com.project.learn.main.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity,Long> {


    List<UserEntity> findByUserName(String userName);

}
