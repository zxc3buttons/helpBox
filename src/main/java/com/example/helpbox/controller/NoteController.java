package com.example.helpbox.controller;

import com.example.helpbox.model.Note;
import com.example.helpbox.model.User;
import com.example.helpbox.repository.NoteRepository;
import com.example.helpbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping("/auth")
public class NoteController {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteController (NoteRepository noteRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return user;
    }

    @GetMapping("/main")
    public String getMainPage(Model model) {
        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
        model.addAttribute("notes", notes);
        return "osnova";
    }

    @PostMapping("/main")
    public String addNote(@RequestParam String title, @RequestParam String text, Model model) {
        User currentUser = getCurrentUser();
        Note note = new Note(text, title, currentUser);
        noteRepository.save(note);
        Iterable <Note> notes = noteRepository.findByAuthor(currentUser);
        model.addAttribute("notes", notes);
        return "osnova";
    }

    @PatchMapping("/{id}")
    public String updateNote (@PathVariable("id") Long id, @RequestParam String title, @RequestParam String text, Model model) {
        Optional <Note> optionalNote = noteRepository.findById(id);
        Note note = optionalNote.get();
        note.setTitle(title);
        note.setText(text);
        note.setDate(new Date());
        noteRepository.save(note);
        return "redirect:/auth/main";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        noteRepository.deleteById(id);
        return "redirect:/auth/main";
    }

}
