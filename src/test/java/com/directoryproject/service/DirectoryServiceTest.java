package com.directoryproject.service;

import com.directoryproject.model.Directory;
import com.directoryproject.repository.DirectoryRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class DirectoryServiceTest {

    @Mock
    private DirectoryRepository directoryRepository;

    @InjectMocks
    private DirectoryService directoryService;

    private static List<Directory> directories = new ArrayList<>();

    @BeforeAll
    public static void beforeAll(){
       Directory firstDirectory = new Directory();
       firstDirectory.setDirectoryName("first directory");
       firstDirectory.setId(1L);
       firstDirectory.setIsValid(true);
       Directory secondDirectory = new Directory();
       secondDirectory.setDirectoryName("second directory");
       secondDirectory.setId(2L);
       secondDirectory.setIsValid(true);
       directories.add(firstDirectory);
       directories.add(secondDirectory);
    }

    @Test
    public void testGetAllDirectoryList() {
        Mockito.when(directoryRepository.findAll()).thenReturn(directories);
        List<Directory> result = directoryService.getAllDirectoryList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(directories.size(), result.size());
    }

    @Test
    public void testGetDirectoryById() {
        Directory directoryTest = directories.get(0);
        Mockito.when(directoryRepository.findById(directoryTest.getId())).thenReturn(Optional.of(directoryTest));
        Optional<Directory> result = directoryService.getDirectoryById(directoryTest.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(directoryTest, result.get());
    }

    @Test
    public void testCreateDirectory() {
        Mockito.when(directoryRepository.save(any())).thenReturn(directories.get(0));
        directoryService.createDirectory(directories.get(0));

        Mockito.verify(directoryRepository, Mockito.times(1)).save(any());
    }

    /*@Test
    public void testDeleteDirectoryById() {
        Mockito.when(directoryService.getDirectoryById(anyLong())).thenReturn(Optional.of(directories.get(0)));
        directoryService.deleteDirectoryById(directories.get(0).getId());

        Mockito.verify(directoryRepository, Mockito.times(1)).deleteById(1L);
    }*/
}
