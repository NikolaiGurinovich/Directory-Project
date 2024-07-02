package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.dto.GetDirectoryInfoDto;
import com.directoryproject.service.DirectoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(value = DirectoryController.class)
public class DirectoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DirectoryService directoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<Directory> directories = new ArrayList<>();

    private final GetDirectoryInfoDto getDirectoryInfoDto = new GetDirectoryInfoDto();

    @BeforeEach
    public void setUp() {
        Directory directory = new Directory();
        directory.setId(1L);
        directory.setDirectoryName("test");
        directories.add(directory);
        Directory directory2 = new Directory();
        directory2.setId(2L);
        directory2.setDirectoryName("test2");
        directories.add(directory2);

        getDirectoryInfoDto.setDirectoryName("test");
        getDirectoryInfoDto.setId(1L);
    }

    @Test
    public void testGetAllDirectoryList() throws Exception {
        Mockito.when(directoryService.getAllDirectoryList()).thenReturn(directories);

        mvc.perform(get("/directory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(directories.size()));
    }

    @Test
    public void testGetDirectoryInfoById() throws Exception {
        Mockito.when(directoryService.getDirectoryInfoById(anyLong())).
                thenReturn(Optional.of(getDirectoryInfoDto));
        Mockito.when(directoryService.getDirectoryById(anyLong())).
                thenReturn(Optional.of(directories.get(0)));

        mvc.perform(get("/directory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateDirectory() throws Exception {
        Mockito.when(directoryService.createDirectory(directories.get(0))).thenReturn(true);
        mvc.perform(post("/directory")
                        .content(objectMapper.writeValueAsString(directories.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateDirectoryById() throws Exception {
        Mockito.when(directoryService.updateDirectoryById(directories.get(0).getId(), directories.get(0)))
                .thenReturn(true);
        Mockito.when(directoryService.getDirectoryById(anyLong())).thenReturn(Optional.ofNullable(directories.get(0)));

        mvc.perform(put("/directory/1")
                        .content(objectMapper.writeValueAsString(directories.get(0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteDirectory() throws Exception {
        Mockito.when(directoryService.deleteDirectoryById(anyLong())).thenReturn(true);
        Mockito.when(directoryService.getDirectoryById(anyLong())).thenReturn(Optional.ofNullable(directories.get(0)));

        mvc.perform(delete("/directory/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateDirectoryInvalid() throws Exception {
        Mockito.when(directoryService.getDirectoryById(anyLong())).thenReturn(Optional.ofNullable(directories.get(0)));
        Mockito.when(directoryService.updateDirectoryInvalid(anyLong())).thenReturn(true);

        mvc.perform(put("/directory/invalid/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateDirectoryValid() throws Exception {
        Mockito.when(directoryService.getDirectoryById(anyLong())).thenReturn(Optional.ofNullable(directories.get(0)));
        Mockito.when(directoryService.updateDirectoryValid(anyLong())).thenReturn(true);

        mvc.perform(put("/directory/valid/1"))
                .andExpect(status().isNoContent());
    }
}
