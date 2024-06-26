package com.directoryproject.service;

import com.directoryproject.model.Directory;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.repository.DateInfoRepository;
import com.directoryproject.repository.DirectoryRepository;
import com.directoryproject.repository.NumberInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NumberInfoService {

    private final NumberInfoRepository numberInfoRepository;
    private final DirectoryService directoryService;
    private final DirectoryRepository directoryRepository;

    @Autowired
    public NumberInfoService(NumberInfoRepository numberInfoRepository, DirectoryService directoryService,
                             DirectoryRepository directoryRepository) {
        this.numberInfoRepository = numberInfoRepository;
        this.directoryService = directoryService;
        this.directoryRepository = directoryRepository;
    }

    public List<NumberInfo> getAllNumberInfo() {
        return numberInfoRepository.findAll();
    }

    public Optional<NumberInfo> getNumberInfoById(Long id) {
        return numberInfoRepository.findById(id);
    }

    public Boolean addNumber (Long id, NumberInfo numberInfo) {
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            return false;
        }
        NumberInfo newNumberInfo = new NumberInfo();
        newNumberInfo.setDirectoryId(id);
        newNumberInfo.setNumber(numberInfo.getNumber());
        newNumberInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newNumberInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        NumberInfo savedNumberInfo = numberInfoRepository.save(newNumberInfo);
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        return numberInfoRepository.existsById(savedNumberInfo.getId());
    }

    public Boolean deleteNumberById(Long id) {
        Optional<NumberInfo> numberInfo = numberInfoRepository.findById(id);
        if (numberInfo.isEmpty()) {
            return false;
        }
        Optional<Directory> directory = directoryService.getDirectoryById(numberInfo.get().getDirectoryId());
        if (directory.isEmpty()) {
            return false;
        }
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        numberInfoRepository.deleteById(id);
        return getNumberInfoById(id).isEmpty();
    }
}
