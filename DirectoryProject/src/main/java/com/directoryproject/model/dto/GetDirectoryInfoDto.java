package com.directoryproject.model.dto;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.model.TextInfo;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class GetDirectoryInfoDto {

    private Long id;

    private String directoryName;

    private List<TextInfo> textInfo;

    private List<NumberInfo> numberInfo;

    private List<DateInfo> dateInfo;

    private Timestamp created;

    private Timestamp updated;
}
