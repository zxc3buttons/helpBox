package com.example.helpbox.repository;

import com.example.helpbox.model.Note;
import com.example.helpbox.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoteRepository extends CrudRepository <Note, Long> {
    public Iterable <Note> findByAuthor(User user);
}
