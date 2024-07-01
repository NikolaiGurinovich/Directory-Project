package com.directoryproject.service;

import com.directoryproject.model.TextInfo;
import com.directoryproject.repository.TextInfoRepository;
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

@ExtendWith(MockitoExtension.class)
public class TextInfoServiceTest {

    @Mock
    private TextInfoRepository textInfoRepository;

    @Mock
    private DirectoryService directoryService;

    @InjectMocks
    private TextInfoService textInfoService;

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
    public void testGetAllTextInfo() {
        Mockito.when(textInfoRepository.findAll()).thenReturn(textInfos);
        List<TextInfo> textInfoList = textInfoService.getAllTextInfo();

        Mockito.verify(textInfoRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(textInfoList.size(), textInfoList.size());
    }

    @Test
    public void testGetTextInfoById() {
        Mockito.when(textInfoRepository.findById(textInfos.get(0).getId())).thenReturn(Optional.of(textInfos.get(0)));
        Optional<TextInfo> result = textInfoService.getTextInfoById(textInfos.get(0).getId());

        Mockito.verify(textInfoRepository, Mockito.times(1)).findById(textInfos.get(0).getId());
        Assertions.assertEquals(textInfos.get(0), result.get());
    }

    @Test
    public void testAddTextInfo() {
        Mockito.when(textInfoRepository.save(Mockito.any(TextInfo.class))).thenReturn(textInfos.get(0));
        textInfoService.addText(textInfos.get(0).getDirectoryId(), textInfos.get(0));

        Mockito.verify(textInfoRepository, Mockito.times(1)).save(Mockito.any(TextInfo.class));
    }

    @Test
    public void testDeleteTextInfoById() {
        Mockito.when(textInfoRepository.findById(textInfos.get(0).getId())).thenReturn(Optional.of(textInfos.get(0)));
        textInfoService.deleteTextInfoById(textInfos.get(0).getId());

        Mockito.verify(textInfoRepository, Mockito.times(1)).deleteById(textInfos.get(0).getId());
    }

    @Test
    public void testUpdateTextInfoById() {
        Mockito.when(textInfoRepository.findById(textInfos.get(0).getId())).thenReturn(Optional.of(textInfos.get(0)));
        Mockito.when(textInfoRepository.save(Mockito.any(TextInfo.class))).thenReturn(textInfos.get(0));
        textInfoService.updateTextInfoById(textInfos.get(0).getId(), textInfos.get(1));

        Mockito.verify(textInfoRepository, Mockito.times(1)).save(textInfos.get(0));
    }
}
