package com.project.learn.main.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Table(name = "post_details")
@Entity(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private String link;
    private String acceptedBy;

}
