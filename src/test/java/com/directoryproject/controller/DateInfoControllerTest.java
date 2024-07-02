package com.directoryproject.controller;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.Directory;
import com.directoryproject.service.DateInfoService;
import com.directoryproject.service.DirectoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = DateInfoController.class)
public class DateInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DateInfoService dateInfoService;

    @MockBean
    private DirectoryService directoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<DateInfo> dateInfos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        DateInfo dateInfo = new DateInfo();
        dateInfo.setId(1L);
        dateInfo.setDirectoryId(1L);
        dateInfo.setDate(Timestamp.valueOf(LocalDateTime.now()));
        dateInfos.add(dateInfo);
        DateInfo dateInfo2 = new DateInfo();
        dateInfo2.setId(2L);
        dateInfo2.setDirectoryId(2L);
        dateInfo2.setDate(Timestamp.valueOf(LocalDateTime.now()));
        dateInfos.add(dateInfo2);
    }

    @Test
    public void testGetAllDateInfo() throws Exception {
        Mockito.when(dateInfoService.getAllDateInfo()).thenReturn(dateInfos);

        mvc.perform(get("/date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(dateInfos.size()));
    }

    @Test
    public void testGetDateInfoById() throws Exception {
        Mockito.when(dateInfoService.getDateInfoById(anyLong())).thenReturn(Optional.ofNullable(dateInfos.get(0)));

        mvc.perform(get("/date/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Disabled
    @Test
    public void testAddDateInfo() throws Exception {
        Directory directory = new Directory();
        directory.setId(1L);
        Mockito.when(directoryService.getDirectoryById(directory.getId())).thenReturn(Optional.of(directory));
        Mockito.when(dateInfoService.addDateInfo(directory.getId(), dateInfos.get(0)))
                .thenReturn(true);

        mvc.perform(post("/date/1")
                        .content(objectMapper.writeValueAsString(dateInfos.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Disabled
    @Test
    public void testUpdateDateInfoById() throws Exception {
        Mockito.when(dateInfoService.getDateInfoById(dateInfos.get(0).getId())).thenReturn(Optional.of(dateInfos.get(0)));
        Mockito.when(dateInfoService.updateDateInfoById(dateInfos.get(0).getId(), dateInfos.get(0))).thenReturn(true);

        mvc.perform(post("/date/1")
                        .content(objectMapper.writeValueAsString(dateInfos.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteDateInfoById() throws Exception {
        Mockito.when(dateInfoService.getDateInfoById(anyLong())).thenReturn(Optional.ofNullable(dateInfos.get(0)));
        Mockito.when(dateInfoService.deleteDateInfoById(anyLong())).thenReturn(true);

        mvc.perform(delete("/date/1"))
                .andExpect(status().isNoContent());
    }
}
