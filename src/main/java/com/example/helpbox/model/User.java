package com.example.helpbox.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data
@Entity
@SequenceGenerator(name = "seq", sequenceName = "users_seq", initialValue = 1, allocationSize = 1)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column
    private String password;

    @Column
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Status status;
}
