package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssignee;
import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssignee.CurrentBirthdayAssigneeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentBirthdayAssigneesRepository extends CrudRepository<CurrentBirthdayAssignee, CurrentBirthdayAssigneeId> {
    Iterable<CurrentBirthdayAssignee> getAllByBirthdays_Guild_Enabled(Boolean birthdays_guild_enabled);
}
