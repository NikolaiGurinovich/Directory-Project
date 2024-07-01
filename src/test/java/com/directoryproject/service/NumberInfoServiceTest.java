package com.directoryproject.service;

import com.directoryproject.model.NumberInfo;
import com.directoryproject.repository.NumberInfoRepository;
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
public class NumberInfoServiceTest {
    @Mock
    private NumberInfoRepository numberInfoRepository;

    @Mock
    private DirectoryService directoryService;

    @InjectMocks
    private NumberInfoService numberInfoService;

    private final List<NumberInfo> numberInfos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        NumberInfo numberInfo = new NumberInfo();
        numberInfo.setId(1L);
        numberInfo.setNumber(234D);
        numberInfo.setDirectoryId(1L);
        numberInfos.add(numberInfo);
        NumberInfo numberInfo1 = new NumberInfo();
        numberInfo1.setId(2L);
        numberInfo1.setNumber(123D);
        numberInfo1.setDirectoryId(2L);
        numberInfos.add(numberInfo1);
    }

    @Test
    public void testGetAllNumberInfo() {
        Mockito.when(numberInfoRepository.findAll()).thenReturn(numberInfos);
        List<NumberInfo> allNumberInfos = numberInfoService.getAllNumberInfo();

        Mockito.verify(numberInfoRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(numberInfos.size(), allNumberInfos.size());
    }

    @Test
    public void testGetNumberInfoById() {
        Mockito.when(numberInfoRepository.findById(numberInfos.get(0).getId())).
                thenReturn(Optional.of(numberInfos.get(0)));
        Optional<NumberInfo> result = numberInfoService.getNumberInfoById(numberInfos.get(0).getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(numberInfos.get(0), result.get());
        Mockito.verify(numberInfoRepository, Mockito.times(1)).findById(numberInfos.get(0).getId());
    }

    @Test
    public void testAddNumberInfo() {
        Mockito.when(numberInfoRepository.save(any())).thenReturn(numberInfos.get(0));
        numberInfoService.addNumber(numberInfos.get(0).getDirectoryId(), numberInfos.get(0));

        Mockito.verify(numberInfoRepository, Mockito.times(1)).existsById(numberInfos.get(0).getId());
    }

    @Test
    public void testDeleteNumberInfoById() {
        Mockito.when(numberInfoRepository.findById(numberInfos.get(0).getId())).thenReturn(Optional.of(numberInfos.get(0)));
        numberInfoService.deleteNumberById(numberInfos.get(0).getId());

        Mockito.verify(numberInfoRepository, Mockito.times(1)).deleteById(numberInfos.get(0).getId());
    }

    @Test
    public void testUpdateNumberInfoById() {
        Mockito.when(numberInfoRepository.findById(numberInfos.get(0).getId())).thenReturn(Optional.of(numberInfos.get(0)));
        Mockito.when(numberInfoRepository.save(any())).thenReturn(numberInfos.get(0));
        numberInfoService.updateNumberById(numberInfos.get(0).getId(), numberInfos.get(1));

        Mockito.verify(numberInfoRepository, Mockito.times(1)).save(numberInfos.get(0));
    }
}
