package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ASSIGNED_ROLES", schema = "BIRTHDAYBOT")
public class AssignedRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GUILD_ID", nullable = false)
    private Long id;

    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;

}