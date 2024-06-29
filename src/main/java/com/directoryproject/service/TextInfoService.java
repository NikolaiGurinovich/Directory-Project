package com.directoryproject.service;

import com.directoryproject.model.TextInfo;
import com.directoryproject.repository.TextInfoRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    public TextInfoService(TextInfoRepository textInfoRepository, DirectoryService directoryService) {
        this.textInfoRepository = textInfoRepository;
        this.directoryService = directoryService;
    }

    public List<TextInfo> getAllTextInfo() {
        return textInfoRepository.findAll();
    }

    public Optional<TextInfo> getTextInfoById(Long id) {
        return textInfoRepository.findById(id);
    }

    @Transactional
    public Boolean addText(Long id, TextInfo textInfo) {
        TextInfo newTextInfo = new TextInfo();
        newTextInfo.setDirectoryId(id);
        newTextInfo.setText(textInfo.getText());
        newTextInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newTextInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        TextInfo savedTextInfo = textInfoRepository.save(newTextInfo);
        if (directoryService.changeUpdated(id)){
            return false;
        }
        return textInfoRepository.existsById(savedTextInfo.getId());
    }

    @Transactional
    public Boolean deleteTextInfoById(Long id) {
        Optional<TextInfo> textInfo = textInfoRepository.findById(id);
        if (textInfo.isEmpty()) {
            log.error("Text info not found");
            return false;
        }
        textInfoRepository.deleteById(id);
        if (directoryService.changeUpdated(textInfo.get().getDirectoryId())){
            return false;
        }
        return getTextInfoById(id).isEmpty();
    }

    @Transactional
    public Boolean updateTextInfoById(Long id, TextInfo textInfo) {
        Optional<TextInfo> textInfoOptional = textInfoRepository.findById(id);
        if (textInfoOptional.isEmpty()) {
            log.error("Text info not found");
            return false;
        }
        TextInfo textInfoToUpdate = textInfoOptional.get();
        textInfoToUpdate.setText(textInfo.getText());
        textInfoToUpdate.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        TextInfo savedTextInfo = textInfoRepository.save(textInfoToUpdate);
        if (directoryService.changeUpdated(textInfoToUpdate.getDirectoryId())){
            return false;
        }
        return savedTextInfo.equals(textInfoToUpdate);
    }
}
