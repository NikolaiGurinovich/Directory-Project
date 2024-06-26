package com.directoryproject.service;

import com.directoryproject.model.Directory;
import com.directoryproject.model.TextInfo;
import com.directoryproject.repository.DirectoryRepository;
import com.directoryproject.repository.TextInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TextInfoService {

    private final TextInfoRepository textInfoRepository;
    private final DirectoryService directoryService;
    private final DirectoryRepository directoryRepository;

    @Autowired
    public TextInfoService(TextInfoRepository textInfoRepository, DirectoryService directoryService,
                           DirectoryRepository directoryRepository) {
        this.textInfoRepository = textInfoRepository;
        this.directoryService = directoryService;
        this.directoryRepository = directoryRepository;
    }

    public List<TextInfo> getAllTextInfo() {
        return textInfoRepository.findAll();
    }

    public Optional<TextInfo> getTextInfoById(Long id) {
        return textInfoRepository.findById(id);
    }

    public Boolean addText(Long id, TextInfo textInfo) {
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return false;
        }
        TextInfo newTextInfo = new TextInfo();
        newTextInfo.setDirectoryId(id);
        newTextInfo.setText(textInfo.getText());
        newTextInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newTextInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        TextInfo savedTextInfo = textInfoRepository.save(newTextInfo);
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        return textInfoRepository.existsById(savedTextInfo.getId());
    }

    public Boolean deleteTextInfoById(Long id) {
        Optional<TextInfo> textInfo = textInfoRepository.findById(id);
        if (textInfo.isEmpty()) {
            log.error("Text info not found");
            return false;
        }
        textInfoRepository.deleteById(id);
        Optional<Directory> directory = directoryService.getDirectoryById(textInfo.get().getDirectoryId());
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return false;
        }
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        return getTextInfoById(id).isEmpty();
    }
}
