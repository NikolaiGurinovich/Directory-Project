package com.directoryproject.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CreateDirectoryDto {
    @NotNull
    private String directoryName;

    private String text;

    private Double number;

    private Timestamp date;
}
