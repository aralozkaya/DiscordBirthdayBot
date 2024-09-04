package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class BirthdayId implements java.io.Serializable {
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