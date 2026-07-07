package com.as.journal.repository;

import com.as.journal.entity.JournalEntry;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
}
