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

import java.util.*;


@Controller
@RequestMapping("/main")
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

    @GetMapping("/")
    public String getMainPage(Model model) {
        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
        model.addAttribute("notes", notes);
        model.addAttribute("user", getCurrentUser());
        return "osnova";
    }

    @GetMapping("/{id}")
    public String editNote (@PathVariable("id") Long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow();
        model.addAttribute("note", note);
        model.addAttribute("user", getCurrentUser());
        return "update";
    }

    @GetMapping("/find")
    public String getFindPage(Model model) {
        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
        model.addAttribute("notes", notes);
        model.addAttribute("user", getCurrentUser());
        return "osnova";
    }

    @PostMapping("/find")
    public String findNote(@RequestParam String title, Model model) {
        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
        List <Note> result = new ArrayList<>();
        if (title.equals("")) {
            return "redirect:/main";
        }
        else {
            for (Note note : notes) {
                if (note.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    result.add(note);
                }
            }
        }
        if (result.isEmpty()) model.addAttribute("exception", "По Вашему запросу ничего не найдено");
        model.addAttribute("notes", result);
        model.addAttribute(getCurrentUser());
        return "osnova";
    }

    @PostMapping("/")
    public String addNote(@RequestParam String title, @RequestParam String text, Model model) {
        User currentUser = getCurrentUser();
        Note note = new Note(text, title, currentUser);
        noteRepository.save(note);
        Iterable <Note> notes = noteRepository.findByAuthor(currentUser);
        model.addAttribute("notes", notes);
        return "redirect:/main";
    }

    @PutMapping("/{id}")
    public String updateNote (@PathVariable("id") Long id, @RequestParam(required = false) String title, @RequestParam String text) {
        Optional <Note> optionalNote = noteRepository.findById(id);
        Note note = optionalNote.orElseThrow();
        note.setTitle(title);
        note.setText(text);
        note.setDate(new Date());
        noteRepository.save(note);
        return "redirect:/main";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        noteRepository.deleteById(id);
        return "redirect:/main";
    }

}
