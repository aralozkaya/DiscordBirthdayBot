package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.Birthday;
import com.aralozkaya.discordbirthdaybot.dbo.BirthdayId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdaysRepository extends CrudRepository<Birthday, BirthdayId> {
}
