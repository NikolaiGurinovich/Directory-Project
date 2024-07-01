package com.directoryproject.service;

import com.directoryproject.model.DateInfo;
import com.directoryproject.repository.DateInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DateInfoServiceTest {

    @Mock
    private DateInfoRepository dateInfoRepository;

    @Mock
    private DirectoryService directoryService;

    @InjectMocks
    private DateInfoService dateInfoService;

    private final List<DateInfo> dateInfos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        DateInfo dateInfo = new DateInfo();
        dateInfo.setId(1L);
        dateInfo.setDate(Timestamp.valueOf(LocalDateTime.now()));
        dateInfo.setDirectoryId(1L);
        dateInfos.add(dateInfo);
        DateInfo dateInfo2 = new DateInfo();
        dateInfo2.setId(2L);
        dateInfo2.setDate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        dateInfo2.setDirectoryId(2L);
        dateInfos.add(dateInfo2);
    }

    @Test
    public void testGetAllDateInfo() {
        Mockito.when(dateInfoRepository.findAll()).thenReturn(dateInfos);
        List<DateInfo> allDateInfos = dateInfoService.getAllDateInfo();

        Mockito.verify(dateInfoRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(dateInfos.size(), allDateInfos.size());
    }

    @Test
    public void testGetDateInfoById() {
        Mockito.when(dateInfoRepository.findById(dateInfos.get(0).getId())).thenReturn(Optional.of(dateInfos.get(0)));
        Optional<DateInfo> result = dateInfoService.getDateInfoById(dateInfos.get(0).getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dateInfos.get(0), result.get());
        Mockito.verify(dateInfoRepository, Mockito.times(1)).findById(dateInfos.get(0).getId());
    }

    @Test
    public void testAddDateInfo() {
        Mockito.when(dateInfoRepository.save(any())).thenReturn(dateInfos.get(0));
        dateInfoService.addDateInfo(dateInfos.get(0).getDirectoryId(), dateInfos.get(0));

        Mockito.verify(dateInfoRepository, Mockito.times(1)).existsById(dateInfos.get(0).getId());
    }

    @Test
    public void testDeleteDateInfoById() {
        Mockito.when(dateInfoRepository.findById(dateInfos.get(0).getId())).thenReturn(Optional.of(dateInfos.get(0)));
        dateInfoService.deleteDateInfoById(dateInfos.get(0).getId());

        Mockito.verify(dateInfoRepository, Mockito.times(1)).deleteById(dateInfos.get(0).getId());
    }

    @Test
    public void testUpdateDateInfoById() {
        Mockito.when(dateInfoRepository.findById(dateInfos.get(0).getId())).thenReturn(Optional.of(dateInfos.get(0)));
        Mockito.when(dateInfoRepository.save(any())).thenReturn(dateInfos.get(0));
        dateInfoService.updateDateInfoById(dateInfos.get(0).getId(), dateInfos.get(1));

        Mockito.verify(dateInfoRepository, Mockito.times(1)).save(dateInfos.get(0));
    }
}
