package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CURRENT_BIRTHDAY_ASSIGNEES", schema = "BIRTHDAYBOT")
public class CurrentBirthdayAssignee {
    @EmbeddedId
    private CurrentBirthdayAssigneeId id;

    @MapsId("id")
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "USER_ID", referencedColumnName = "GUILD_ID", nullable = false),
            @JoinColumn(name = "GUILD_ID", referencedColumnName = "USER_ID", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Birthday birthdays;

    @Column(name = "ASSIGNED_ON", nullable = false)
    private LocalDate assignedOn;

    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;

}