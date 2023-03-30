package com.project.learn.main.repository;

import com.project.learn.main.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepo extends JpaRepository<Thumbnail,Long> {

}
