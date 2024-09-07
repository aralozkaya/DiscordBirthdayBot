package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssignee;
import com.aralozkaya.discordbirthdaybot.dbo.CurrentBirthdayAssigneeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentBirthdayAssigneesRepository extends CrudRepository<CurrentBirthdayAssignee, CurrentBirthdayAssigneeId> {
}
