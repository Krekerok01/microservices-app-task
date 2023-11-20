package com.specificgroup.blog.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postsSeqGenerator")
    @SequenceGenerator(name = "postsSeqGenerator", sequenceName = "posts_seq", allocationSize = 1)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "modification_date")
    private LocalDateTime modificationDate;
}