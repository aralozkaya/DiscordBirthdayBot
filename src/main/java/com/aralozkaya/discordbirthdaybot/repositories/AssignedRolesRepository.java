package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.AssignedRole;
import org.springframework.data.repository.CrudRepository;

public interface AssignedRolesRepository extends CrudRepository<AssignedRole, Long> {
}
