package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.Guild;
import org.springframework.data.repository.CrudRepository;

public interface GuildsRepository extends CrudRepository<Guild, Long> {
}
