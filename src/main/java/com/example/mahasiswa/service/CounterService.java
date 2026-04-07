package com.example.mahasiswa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import com.example.mahasiswa.model.Counter;

@Service
public class CounterService {

    @Autowired
    private MongoOperations mongoOperations;

    public long getNextSequence(String seqName) {
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);

        Counter counter = mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class
        );

        return counter.getSeq();
    }
}