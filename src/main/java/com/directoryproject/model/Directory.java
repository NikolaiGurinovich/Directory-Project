package com.directoryproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "directory_list")
public class Directory {
    @Id
    @SequenceGenerator(name = "date_infoSeqGen", sequenceName = "date_info_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "date_infoSeqGen")
    private Long id;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;

    @Column(name = "is_valid")
    private Boolean isValid;

    @NotNull
    @Column(name = "dir_name")
    private String directoryName;
}
