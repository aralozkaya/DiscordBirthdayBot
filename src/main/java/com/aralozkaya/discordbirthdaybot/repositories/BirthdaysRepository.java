package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.Birthday;
import com.aralozkaya.discordbirthdaybot.dbo.Birthday.BirthdayId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BirthdaysRepository extends CrudRepository<Birthday, BirthdayId> {
    @Query("SELECT b FROM Birthday b WHERE b.guild.enabled = :enabled AND b.id.userId not in (SELECT cba.id.userId FROM CurrentBirthdayAssignee cba WHERE cba.id.guildId = b.id.guildId)")
    List<Birthday> getAllNotCurrentlyAssignedBirthdaysByGuild_Enabled (@Param("enabled") boolean enabled);
    void deleteById_GuildIdAndId_UserId(Long id_guildId, Long id_userId);
}
