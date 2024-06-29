package com.directoryproject.service;

import com.directoryproject.model.NumberInfo;
import com.directoryproject.repository.NumberInfoRepository;
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
public class NumberInfoService {

    private final NumberInfoRepository numberInfoRepository;
    private final DirectoryService directoryService;

    @Autowired
    public NumberInfoService(NumberInfoRepository numberInfoRepository, DirectoryService directoryService) {
        this.numberInfoRepository = numberInfoRepository;
        this.directoryService = directoryService;
    }

    public List<NumberInfo> getAllNumberInfo() {
        return numberInfoRepository.findAll();
    }

    public Optional<NumberInfo> getNumberInfoById(Long id) {
        return numberInfoRepository.findById(id);
    }

    @Transactional
    public Boolean addNumber (Long id, NumberInfo numberInfo) {
        NumberInfo newNumberInfo = new NumberInfo();
        newNumberInfo.setDirectoryId(id);
        newNumberInfo.setNumber(numberInfo.getNumber());
        newNumberInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newNumberInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        NumberInfo savedNumberInfo = numberInfoRepository.save(newNumberInfo);
        if (directoryService.changeUpdated(id)){
            return false;
        }
        return numberInfoRepository.existsById(savedNumberInfo.getId());
    }

    @Transactional
    public Boolean deleteNumberById(Long id) {
        Optional<NumberInfo> numberInfo = numberInfoRepository.findById(id);
        if (numberInfo.isEmpty()) {
            log.error("Number info not found");
            return false;
        }
        if (directoryService.changeUpdated(numberInfo.get().getDirectoryId()))
            return false;
        numberInfoRepository.deleteById(id);
        return getNumberInfoById(id).isEmpty();
    }

    @Transactional
    public Boolean updateNumberById(Long id, NumberInfo numberInfo) {
        Optional<NumberInfo> numberInfoOptional = numberInfoRepository.findById(id);
        if (numberInfoOptional.isEmpty()) {
            log.error("Number info not found");
            return false;
        }
        NumberInfo newNumberInfo = numberInfoOptional.get();
        newNumberInfo.setNumber(numberInfo.getNumber());
        newNumberInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        NumberInfo savedNumberInfo = numberInfoRepository.save(newNumberInfo);
        if (directoryService.changeUpdated(numberInfo.getDirectoryId()))
            return false;
        return getNumberInfoById(id).equals(savedNumberInfo);
    }
}
