package com.directoryproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "text_info")
public class TextInfo {
    @Id
    @SequenceGenerator(name = "text_infoSeqGen", sequenceName = "text_info_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "text_infoSeqGen")
    private Long id;

    @NotNull
    @Column(name = "dir_id")
    private Long directoryId;

    @Column(name = "text")
    private String text;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;
}
