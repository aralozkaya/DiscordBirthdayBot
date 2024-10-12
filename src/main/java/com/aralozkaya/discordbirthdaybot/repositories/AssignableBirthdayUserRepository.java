package com.aralozkaya.discordbirthdaybot.repositories;

import com.aralozkaya.discordbirthdaybot.dbo.AssignableBirthdayUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AssignableBirthdayUserRepository extends PagingAndSortingRepository<AssignableBirthdayUser, AssignableBirthdayUser.AssignableBirthdayUserId> {
    Iterable<AssignableBirthdayUser> findAll();
}
