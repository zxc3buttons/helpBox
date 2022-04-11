package com.example.helpbox.controller;

import com.example.helpbox.exception.NoteNotFoundException;
import com.example.helpbox.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    public NoteController (NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity getNotes() {
        return ResponseEntity.ok(noteService.getUserNotes());
    }

    @PostMapping("/add")
    public ResponseEntity addNote(@RequestParam String title, @RequestParam String text) {
        return ResponseEntity.ok(noteService.addNote(title, text));
    }

    @PostMapping("/find")
    public ResponseEntity findNote(@RequestParam String title) {
        try {
            return ResponseEntity.ok(noteService.findNote(title));
        } catch (NoteNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateNote(@PathVariable Long id, @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String text) {
        try {
            return ResponseEntity.ok(noteService.updateNote(id, title, text));
        } catch (NoteNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @GetMapping("/main/")
//    public String getMainPage(Model model) {
//        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
//        model.addAttribute("notes", notes);
//        model.addAttribute("user", getCurrentUser());
//        return "osnova";
//    }
//
//    @GetMapping("/main/{id}")
//    public String editNote (@PathVariable("id") Long id, Model model) {
//        Note note = noteRepository.findById(id).orElseThrow();
//        model.addAttribute("note", note);
//        model.addAttribute("user", getCurrentUser());
//        return "update";
//    }
//
//    @GetMapping("/main/find")
//    public String getFindPage(Model model) {
//        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
//        model.addAttribute("notes", notes);
//        model.addAttribute("user", getCurrentUser());
//        return "osnova";
//    }
//
//    @PostMapping("/main/find")
//    public String findNote(@RequestParam String title, Model model) {
//        Iterable <Note> notes = noteRepository.findByAuthor(getCurrentUser());
//        List <Note> result = new ArrayList<>();
//        if (title.equals("")) {
//            return "redirect:/auth/main/";
//        }
//        else {
//            for (Note note : notes) {
//                if (note.getTitle().toLowerCase().contains(title.toLowerCase())) {
//                    result.add(note);
//                }
//            }
//        }
//        if (result.isEmpty()) model.addAttribute("exception", "По Вашему запросу ничего не найдено");
//        model.addAttribute("notes", result);
//        model.addAttribute(getCurrentUser());
//        return "osnova";
//    }
//
//    @PostMapping("/main")
//    public String addNote(@RequestParam String title, @RequestParam String text, Model model) {
//        User currentUser = getCurrentUser();
//        Note note = new Note(text, title, currentUser);
//        noteRepository.save(note);
//        Iterable <Note> notes = noteRepository.findByAuthor(currentUser);
//        model.addAttribute("notes", notes);
//        return "redirect:/auth/main/";
//    }
//
//    @PutMapping("/{id}")
//    public String updateNote (@PathVariable("id") Long id, @RequestParam(required = false) String title, @RequestParam String text) {
//        Optional <Note> optionalNote = noteRepository.findById(id);
//        Note note = optionalNote.orElseThrow();
//        note.setTitle(title);
//        note.setText(text);
//        note.setDate(new Date());
//        noteRepository.save(note);
//        return "redirect:/auth/main/";
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Long id) {
//        noteRepository.deleteById(id);
//        return "redirect:/auth/main/";
//    }
}
