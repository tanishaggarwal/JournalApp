package com.as.journal.controller;

import com.as.journal.entity.JournalEntry;
import com.as.journal.entity.User;
import com.as.journal.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    // private HashMap<Long, JournalEntry> journalEntries = new HashMap<>();

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesForUser()
    {
         try {
             Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
             String username= authentication.getName();
             return new ResponseEntity<>(journalEntryService.fetchAll(username), HttpStatus.OK);
         }
         catch (Exception e)
         {
             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         }
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry)
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("Trying to create a user journal");
            journalEntryService.insert(entry, username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Optional<JournalEntry> journalEntry = journalEntryService.fetchById(id, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        }
        catch (NoSuchElementException exception)
        {
            return new ResponseEntity<>("No Such Journal Entry Exists", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String id)
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.deleleById(id, username);
        }
        catch(NoSuchElementException exception)
        {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable String id, @RequestBody JournalEntry entry)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            journalEntryService.updateById(entry, username, id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (IllegalAccessException exception)
        {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }
}
