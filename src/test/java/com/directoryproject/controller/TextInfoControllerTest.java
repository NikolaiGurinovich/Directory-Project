package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.model.TextInfo;
import com.directoryproject.service.DirectoryService;
import com.directoryproject.service.NumberInfoService;
import com.directoryproject.service.TextInfoService;
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
@WebMvcTest(value = TextInfoController.class)
public class TextInfoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TextInfoService textInfoService;

    @MockBean
    private DirectoryService directoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<TextInfo> textInfos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        TextInfo textInfo = new TextInfo();
        textInfo.setId(1L);
        textInfo.setDirectoryId(1L);
        textInfo.setText("test");
        textInfos.add(textInfo);
        TextInfo textInfo2 = new TextInfo();
        textInfo2.setId(2L);
        textInfo2.setDirectoryId(2L);
        textInfo2.setText("test2");
        textInfos.add(textInfo2);
    }

    @Test
    public void testGetAlTextInfo() throws Exception {
        Mockito.when(textInfoService.getAllTextInfo()).thenReturn(textInfos);

        mvc.perform(get("/text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(textInfos.size()));
    }

    @Test
    public void testGetTextInfoById() throws Exception {
        Mockito.when(textInfoService.getTextInfoById(anyLong())).thenReturn(Optional.ofNullable(textInfos.get(0)));

        mvc.perform(get("/text/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testAddTextInfo() throws Exception {
        Directory directory = new Directory();
        directory.setId(1L);
        Mockito.when(directoryService.getDirectoryById(directory.getId())).thenReturn(Optional.of(directory));
        Mockito.when(textInfoService.addText(directory.getId(), textInfos.get(0)))
                .thenReturn(true);

        mvc.perform(post("/text/1")
                        .content(objectMapper.writeValueAsString(textInfos.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Disabled
    @Test
    public void testUpdateTextInfoById() throws Exception {
        Mockito.when(textInfoService.getTextInfoById(textInfos.get(0).getId()))
                .thenReturn(Optional.of(textInfos.get(0)));
        Mockito.when(textInfoService.updateTextInfoById(textInfos.get(0).getId(), textInfos.get(0)))
                .thenReturn(true);

        mvc.perform(post("/text/1")
                        .content(objectMapper.writeValueAsString(textInfos.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNumberInfoById() throws Exception {
        Mockito.when(textInfoService.getTextInfoById(anyLong())).thenReturn(Optional.ofNullable(textInfos.get(0)));
        Mockito.when(textInfoService.deleteTextInfoById(anyLong())).thenReturn(true);

        mvc.perform(delete("/text/1"))
                .andExpect(status().isNoContent());
    }
}
