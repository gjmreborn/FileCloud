package com.gjm.file_cloud.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @ManyToMany
    @JoinTable(name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<File> files;

    public User(String username, String password, List<Role> roles, List<File> files) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.files = files;
    }

    public User(String username, String password) {
        this(username, password, null, null);
    }

    public User() {
    }
}
