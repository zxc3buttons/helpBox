package com.example.helpbox.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@SequenceGenerator(name = "sec", sequenceName = "notes_seq", initialValue =     1, allocationSize = 1)
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec")
    private Long id;

    private String text;

    private String title;

    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private User author;

    public Note () {}

    public Note (String text, String title, User user) {
        this.text = text;
        this.title = title;
        this.author = user;
        this.date = new Date();
    }

}
