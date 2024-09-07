package com.aralozkaya.discordbirthdaybot.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class CurrentBirthdayAssigneeId implements java.io.Serializable {
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