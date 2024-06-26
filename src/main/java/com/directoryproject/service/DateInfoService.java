package com.directoryproject.service;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.Directory;
import com.directoryproject.repository.DateInfoRepository;
import com.directoryproject.repository.DirectoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DateInfoService {

    private final DateInfoRepository dateInfoRepository;
    private final DirectoryService directoryService;
    private final DirectoryRepository directoryRepository;

    @Autowired
    public DateInfoService(DateInfoRepository dateInfoRepository, DirectoryService directoryService,
                           DirectoryRepository directoryRepository) {
        this.dateInfoRepository = dateInfoRepository;
        this.directoryService = directoryService;
        this.directoryRepository = directoryRepository;
    }

    public List<DateInfo> getAllDateInfo() {
        return dateInfoRepository.findAll();
    }

    public Optional<DateInfo> getDateInfoById(Long id) {
        return dateInfoRepository.findById(id);
    }

    public Boolean addDateInfo(Long id, DateInfo dateInfo) {
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return false;
        }
        DateInfo newDateInfo = new DateInfo();
        newDateInfo.setDirectoryId(id);
        newDateInfo.setDate(dateInfo.getDate());
        newDateInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newDateInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        DateInfo savedDateInfo = dateInfoRepository.save(newDateInfo);
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        return dateInfoRepository.existsById(savedDateInfo.getId());
    }

    public Boolean deleteDateInfoById(Long id) {
        Optional<DateInfo> dateInfo = dateInfoRepository.findById(id);
        if (dateInfo.isEmpty()) {
            log.error("Date info not found");
            return false;
        }
        dateInfoRepository.deleteById(id);
        Optional<Directory> directory = directoryService.getDirectoryById(dateInfo.get().getDirectoryId());
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return false;
        }
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        return dateInfoRepository.findById(id).isEmpty();
    }
}
