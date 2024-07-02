package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.service.DirectoryService;
import com.directoryproject.service.NumberInfoService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = NumberInfoController.class)
public class NumberInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NumberInfoService numberInfoService;

    @MockBean
    private DirectoryService directoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<NumberInfo> numberInfos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        NumberInfo numberInfo = new NumberInfo();
        numberInfo.setId(1L);
        numberInfo.setDirectoryId(1L);
        numberInfo.setNumber(123D);
        numberInfos.add(numberInfo);
        NumberInfo numberInfo2 = new NumberInfo();
        numberInfo2.setDirectoryId(2L);
        numberInfo2.setNumber(456D);
        numberInfos.add(numberInfo2);
    }

    @Test
    public void testGetAllNumberInfo() throws Exception {
        Mockito.when(numberInfoService.getAllNumberInfo()).thenReturn(numberInfos);

        mvc.perform(get("/number"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(numberInfos.size()));
    }

    @Test
    public void testGetNumberInfoById() throws Exception {
        Mockito.when(numberInfoService.getNumberInfoById(anyLong())).thenReturn(Optional.ofNullable(numberInfos.get(0)));

        mvc.perform(get("/number/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testAddNumberInfo() throws Exception {
        Directory directory = new Directory();
        directory.setId(1L);
        Mockito.when(directoryService.getDirectoryById(directory.getId())).thenReturn(Optional.of(directory));
        Mockito.when(numberInfoService.addNumber(directory.getId(), numberInfos.get(0)))
                .thenReturn(true);

        mvc.perform(post("/number/1")
                        .content(objectMapper.writeValueAsString(numberInfos.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Disabled
    @Test
    public void testUpdateNumberInfoById() throws Exception {
        Mockito.when(numberInfoService.getNumberInfoById(numberInfos.get(0).getId()))
                .thenReturn(Optional.of(numberInfos.get(0)));
        Mockito.when(numberInfoService.updateNumberById(numberInfos.get(0).getId(), numberInfos.get(0)))
                .thenReturn(true);

        mvc.perform(post("/number/1")
                        .content(objectMapper.writeValueAsString(numberInfos.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNumberInfoById() throws Exception {
        Mockito.when(numberInfoService.getNumberInfoById(anyLong())).thenReturn(Optional.ofNullable(numberInfos.get(0)));
        Mockito.when(numberInfoService.deleteNumberById(anyLong())).thenReturn(true);

        mvc.perform(delete("/number/1"))
                .andExpect(status().isNoContent());
    }
}
