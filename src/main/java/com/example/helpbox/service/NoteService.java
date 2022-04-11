package com.example.helpbox.service;

import com.example.helpbox.exception.NoteNotFoundException;
import com.example.helpbox.model.Note;
import com.example.helpbox.model.User;
import com.example.helpbox.repository.NoteRepository;
import com.example.helpbox.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService (NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
    }

    public List<Note> getUserNotes() {
        return noteRepository.findByAuthor(getCurrentUser());
    }

    public Note addNote(String title, String text) {
        return noteRepository.save(new Note(text, title, getCurrentUser()));
    }

    public List<Note> findNote(String title) throws NoteNotFoundException {
        List<Note> userNotes = getUserNotes();
        List<Note> notes = new ArrayList<>();
        if (title.equals("")) {
            throw new NoteNotFoundException("По вашему запросу ничего не найдено!");
        }
        else {
            for (Note note : userNotes) {
                if (note.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    notes.add(note);
                }
            }
        }
        if (notes.isEmpty()) {
            throw new NoteNotFoundException("По вашему запросу ничего не найдено!");
        }
        else {
            return notes;
        }
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public Note updateNote(Long id,@RequestParam(required = false) String title,@RequestParam(required = false) String text) throws NoteNotFoundException {
        Note note;
        if (noteRepository.findById(id).isPresent()) note = noteRepository.findById(id).get();
        else {
            throw new NoteNotFoundException("Cannot find note with this id: " + id);
        }
        note.setTitle(title);
        note.setText(text);
        note.setDate(new Date());
        return noteRepository.save(note);
    }
}
