package com.demidovn.fruitbounty.server.persistence.entities.chat;

import com.demidovn.fruitbounty.server.persistence.converters.attributes.TimestampConverter;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Table(name = "chat")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = TimestampConverter.class)
    @Column(name = "modified_time", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private long modifiedTimeMs;

    @Type(type = "json")
    @Column(name = "chat", columnDefinition = "json", nullable = true)
    private ChatValues chatValues;

    @PrePersist
    @PreUpdate
    private void onCreate() {
        modifiedTimeMs = Instant.now().toEpochMilli();
    }

}
