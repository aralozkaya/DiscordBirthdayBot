package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

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
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GUILD_ID", nullable = false)
    private Guild guild;

    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class BirthdayId implements java.io.Serializable {
        @Serial
        private static final long serialVersionUID = -6418816410788186588L;
        @Column(name = "GUILD_ID", nullable = false)
        private Long guildId;

        @Column(name = "USER_ID", nullable = false)
        private Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
            BirthdayId entity = (BirthdayId) o;
            return Objects.equals(this.guildId, entity.guildId) &&
                    Objects.equals(this.userId, entity.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(guildId, userId);
        }
    }
}