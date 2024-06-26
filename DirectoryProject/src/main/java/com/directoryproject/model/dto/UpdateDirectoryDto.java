package com.directoryproject.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UpdateDirectoryDto {

    private String directoryName;

    private Boolean isValid;

    private Long textId;

    private String text;

    private Long numberId;

    private Double number;

    private Long dateId;

    private Timestamp date;
}
