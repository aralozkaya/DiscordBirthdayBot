package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GUILD_ID", nullable = false)
    private Guild guilds;

    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
}