package com.gjm.file_cloud.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;

    private String role;

    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }
}
