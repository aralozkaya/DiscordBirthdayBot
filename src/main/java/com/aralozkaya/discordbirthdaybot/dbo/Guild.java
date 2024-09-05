package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
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

    @ColumnDefault("TRUE")
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = true;

    public Guild(Long guildID, LocalDate now, Long roleID) {
        this.id = guildID;
        this.joinDate = now;
        this.guildBotRoleId = roleID;
    }
}