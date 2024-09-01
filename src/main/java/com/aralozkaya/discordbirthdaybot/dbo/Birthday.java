package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "BIRTHDAYS", schema = "BIRTHDAYBOT")
public class Birthday {
    @EmbeddedId
    private BirthdayId id;

    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;

}