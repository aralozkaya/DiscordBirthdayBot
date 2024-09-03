package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ASSIGNED_ROLES", schema = "BIRTHDAYBOT")
public class AssignedRole {
    @Id
    @Column(name = "GUILD_ID", nullable = false)
    private Long id;

    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
}