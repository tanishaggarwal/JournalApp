package com.as.journal.service;

import com.as.journal.JournalApplication;
import com.as.journal.entity.JournalEntry;
import com.as.journal.entity.User;
import com.as.journal.repository.JournalEntryRepository;
import com.as.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository; // ideally we should use userService rather than userRepository for better abstraction and easy future updates in userRepository code

    @Transactional
    public void insert(JournalEntry journalEntry, String username)
    {
        Optional<User> user =  userRepository.findByUserName(username);

        if(user.isPresent()) {
            JournalEntry entry = journalEntryRepository.save(journalEntry);
            user.get().getJournalEntryList().add(entry);
            userRepository.save(user.get());
        }
        else {
            throw new NoSuchElementException("No Such User Found!!");
        }
    }

    public Iterable<JournalEntry> fetchAll(String username) throws Exception {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getJournalEntryList();
        }
        else{
            throw new Exception("No Such User Exists!!");
        }
    }

    public Optional<JournalEntry> fetchById(String id, String username)
    {
        Optional<User> user  = userRepository.findByUserName(username);
        List<JournalEntry> list = user.get().getJournalEntryList();

        if (ifExists(id, list)) {
            return journalEntryRepository.findById(id);
        }
        else {
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public void deleleById(String id, String username)
    {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {

            List<JournalEntry> journalEntries = user.get().getJournalEntryList();

            if(ifExists(id, journalEntries))
            {
                Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);
                if(journalEntry.isPresent()) {

                    user.get().getJournalEntryList().removeIf(x-> x.getId().equals(id));
                    userRepository.save(user.get());

                    journalEntryRepository.deleteById(id);
                }
                else {
                    throw new NoSuchElementException("Journal Entry Not Found!!");
                }
            }
            else
                throw new NoSuchElementException("Journal Entry Not Found!!");
        }
        else {
            throw new NoSuchElementException("No Such User Found!!");
        }
    }

    private boolean ifExists(String id, List<JournalEntry> journalEntries)
    {
        for (JournalEntry journalEntry : journalEntries) {
            if (journalEntry.getId().compareTo(id) == 0)
                return true;
        }
        return false;
    }
    public void updateById(JournalEntry entry, String username, String id) throws IllegalAccessException
    {
        Optional<User> user  = userRepository.findByUserName(username);
        if(user.isPresent())
        {
            List<JournalEntry> list = user.get().getJournalEntryList();
            if(ifExists(id, list))
            {
                Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);
                if(journalEntry.isPresent())
                {
                    journalEntry.get().setTitle(entry.getTitle());
                    journalEntry.get().setContent(entry.getContent());
                    journalEntryRepository.save(journalEntry.get());
                }
            }
            else
                throw new IllegalAccessException("UnAuthorized");
        }
    }
}