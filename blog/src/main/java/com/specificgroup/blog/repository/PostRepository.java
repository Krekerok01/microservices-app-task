package com.specificgroup.blog.repository;

import com.specificgroup.blog.entity.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll(Specification<Post> specification);
    void deleteAllByUserId(Long userId);
    List<Post> findAllByUserIdIn(List<Long> userIds);
}