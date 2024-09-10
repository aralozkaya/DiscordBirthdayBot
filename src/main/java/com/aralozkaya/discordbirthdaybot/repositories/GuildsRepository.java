package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.Guild;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuildsRepository extends CrudRepository<Guild, Long> {
    Optional<Guild> findByIdAndEnabled(Long id, Boolean enabled);
}
