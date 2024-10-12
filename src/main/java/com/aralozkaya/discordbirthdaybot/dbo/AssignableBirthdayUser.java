package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "ASSIGNABLE_BIRTHDAY_USERS", schema = "BIRTHDAYBOT")
public class AssignableBirthdayUser {
    @EmbeddedId
    private AssignableBirthdayUserId id;

    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Getter
    @Setter
    @Embeddable
    public static class AssignableBirthdayUserId implements java.io.Serializable {
        @Serial
        private static final long serialVersionUID = 2208882504060389357L;
        @Column(name = "GUILD_ID")
        private Long guildId;

        @Column(name = "USER_ID")
        private Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
            AssignableBirthdayUserId entity = (AssignableBirthdayUserId) o;
            return Objects.equals(this.guildId, entity.guildId) &&
                    Objects.equals(this.userId, entity.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(guildId, userId);
        }

    }
}