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
@Entity(name = "number_info")
public class NumberInfo {
    @Id
    @SequenceGenerator(name = "number_infoSeqGen", sequenceName = "number_info_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "number_infoSeqGen")
    private Long id;

    @NotNull
    @Column(name = "dir_id")
    private Long directoryId;

    @Column(name = "number")
    private Double number;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;
}
