package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "BIRTHDAYS", schema = "BIRTHDAYBOT")
public class Birthday {
    @EmbeddedId
    private BirthdayId id;

    @MapsId("guildId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GUILD_ID", nullable = false)
    private Guild guild;

    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;
}