package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GUILDS", schema = "BIRTHDAYBOT")
public class Guild {
    @Id
    @Column(name = "GUILD_ID", nullable = false)
    private Long id;

    @Column(name = "JOIN_DATE", nullable = false)
    private LocalDate joinDate;

    @Column(name = "GUILD_BOT_ROLE_ID", nullable = false)
    private Long guildBotRoleId;
}