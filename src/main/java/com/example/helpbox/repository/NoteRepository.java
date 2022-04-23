package com.example.helpbox.repository;

import com.example.helpbox.model.Note;
import com.example.helpbox.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository <Note, Long> {
    public List<Note> findByAuthor(User user);
    public Iterable <Note> findByTitle(String title);
}
