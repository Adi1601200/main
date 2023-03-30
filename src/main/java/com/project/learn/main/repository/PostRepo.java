package com.project.learn.main.repository;

import com.project.learn.main.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post,Long> {
    @Override
    Optional<Post> findById(Long aLong);
}
