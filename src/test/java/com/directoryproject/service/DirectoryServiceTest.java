package com.directoryproject.service;

import com.directoryproject.model.Directory;
import com.directoryproject.repository.DirectoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
public class DirectoryServiceTest {

    @Mock
    private DirectoryRepository directoryRepository;

    @InjectMocks
    private DirectoryService directoryService;

    private static List<Directory> directories = new ArrayList<>();

    @BeforeEach
    public void beforeAll(){
       Directory firstDirectory = new Directory();
       firstDirectory.setDirectoryName("first directory");
       firstDirectory.setId(1L);
       firstDirectory.setIsValid(true);
       Directory secondDirectory = new Directory();
       secondDirectory.setDirectoryName("second directory");
       secondDirectory.setId(2L);
       secondDirectory.setIsValid(false);
       directories.add(firstDirectory);
       directories.add(secondDirectory);
    }

    @Test
    public void testGetAllDirectoryList() {
        Mockito.when(directoryRepository.findAll()).thenReturn(directories);
        List<Directory> result = directoryService.getAllDirectoryList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(directories.size(), result.size());
        Mockito.verify(directoryRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetDirectoryById() {
        Directory directoryTest = directories.get(0);
        Mockito.when(directoryRepository.findById(directoryTest.getId())).thenReturn(Optional.of(directoryTest));
        Optional<Directory> result = directoryService.getDirectoryById(directoryTest.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(directoryTest, result.get());
        Mockito.verify(directoryRepository, Mockito.times(1)).findById(directoryTest.getId());
    }

    @Test
    public void testCreateDirectory() {
        Mockito.when(directoryRepository.save(any())).thenReturn(directories.get(0));
        directoryService.createDirectory(directories.get(0));

        Mockito.verify(directoryRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void testDeleteDirectoryById() {
        Mockito.when(directoryRepository.findById(directories.get(0).getId())).
                thenReturn(Optional.of(directories.get(0)));
        directoryService.deleteDirectoryById(directories.get(0).getId());

        Mockito.verify(directoryRepository, Mockito.times(1)).delete(directories.get(0));
    }

    @Test
    public void testUpdateDirectoryById() {
        Mockito.when(directoryRepository.findById(directories.get(0).getId())).
                thenReturn(Optional.of(directories.get(0)));
        Mockito.when(directoryRepository.save(any())).thenReturn(directories.get(0));
        directoryService.updateDirectoryById(directories.get(0).getId(), directories.get(1));

        Mockito.verify(directoryRepository, Mockito.times(1)).save(any(Directory.class));
    }

    @Test
    public void testUpdateDirectoryInvalid() {
        Mockito.when(directoryRepository.findById(directories.get(0).getId())).
                thenReturn(Optional.of(directories.get(0)));
        Mockito.when(directoryRepository.save(any())).thenReturn(directories.get(0));
        directoryService.updateDirectoryInvalid(directories.get(0).getId());

        Mockito.verify(directoryRepository, Mockito.times(1)).save(any(Directory.class));
        Assertions.assertFalse(directories.get(0).getIsValid());
    }

    @Test
    public void testUpdateDirectoryValid() {
        Mockito.when(directoryRepository.findById(directories.get(1).getId())).
                thenReturn(Optional.of(directories.get(1)));
        Mockito.when(directoryRepository.save(any())).thenReturn(directories.get(1));
        directoryService.updateDirectoryValid(directories.get(1).getId());

        Mockito.verify(directoryRepository, Mockito.times(1)).save(any(Directory.class));
        Assertions.assertTrue(directories.get(1).getIsValid());
    }

    @Test
    public void testChangeUpdated(){
        Mockito.when(directoryRepository.findById(directories.get(0).getId())).
                thenReturn(Optional.of(directories.get(0)));
        directoryService.changeUpdated(directories.get(0).getId());

        Mockito.verify(directoryRepository, Mockito.times(1)).save(any(Directory.class));
    }
}
