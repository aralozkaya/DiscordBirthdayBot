package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssignee;
import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssignee.CurrentBirthdayAssigneeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentBirthdayAssigneesRepository extends CrudRepository<CurrentBirthdayAssignee, CurrentBirthdayAssigneeId> {
    List<CurrentBirthdayAssignee> getAllById_GuildId(Long guildId);
    Iterable<CurrentBirthdayAssignee> getAllByBirthdays_Guild_Enabled(Boolean birthdays_guild_enabled);
}
