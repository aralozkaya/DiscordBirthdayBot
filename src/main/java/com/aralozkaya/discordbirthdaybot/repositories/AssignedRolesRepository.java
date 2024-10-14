package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.AssignedRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignedRolesRepository extends CrudRepository<AssignedRole, Long> {
    List<AssignedRole> findAllByGuilds_Enabled(Boolean guilds_enabled);
}
