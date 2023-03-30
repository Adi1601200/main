package com.project.learn.main.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;


}
