package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.Birthday;
import com.aralozkaya.discordbirthdaybot.dbo.Birthday.BirthdayId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdaysRepository extends CrudRepository<Birthday, BirthdayId> {
    Iterable<Birthday> getAllByGuild_Enabled (Boolean enabled);
    void deleteById_GuildIdAndId_UserId(Long id_guildId, Long id_userId);
}
