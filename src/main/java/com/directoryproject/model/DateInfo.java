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
@Entity(name = "date_info")
public class DateInfo {
    @Id
    @SequenceGenerator(name = "date_infoSeqGen", sequenceName = "date_info_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "date_infoSeqGen")
    private Long id;

    @NotNull
    @Column(name = "dir_id")
    private Long directoryId;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;
}
