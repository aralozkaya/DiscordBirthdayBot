package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Objects;

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
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class CurrentBirthdayAssigneeId implements java.io.Serializable {
        private static final long serialVersionUID = 7790071503875812927L;
        @Column(name = "GUILD_ID", nullable = false)
        private Long guildId;

        @Column(name = "USER_ID", nullable = false)
        private Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
            CurrentBirthdayAssigneeId entity = (CurrentBirthdayAssigneeId) o;
            return Objects.equals(this.guildId, entity.guildId) &&
                    Objects.equals(this.userId, entity.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(guildId, userId);
        }
    }
}