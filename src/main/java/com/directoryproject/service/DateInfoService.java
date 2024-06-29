package com.directoryproject.service;

import com.directoryproject.model.DateInfo;
import com.directoryproject.repository.DateInfoRepository;
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
public class DateInfoService {

    private final DateInfoRepository dateInfoRepository;
    private final DirectoryService directoryService;

    @Autowired
    public DateInfoService(DateInfoRepository dateInfoRepository, DirectoryService directoryService) {
        this.dateInfoRepository = dateInfoRepository;
        this.directoryService = directoryService;
    }

    public List<DateInfo> getAllDateInfo() {
        return dateInfoRepository.findAll();
    }

    public Optional<DateInfo> getDateInfoById(Long id) {
        return dateInfoRepository.findById(id);
    }

    @Transactional
    public Boolean addDateInfo(Long id, DateInfo dateInfo) {
        DateInfo newDateInfo = new DateInfo();
        newDateInfo.setDirectoryId(id);
        newDateInfo.setDate(dateInfo.getDate());
        newDateInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newDateInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        DateInfo savedDateInfo = dateInfoRepository.save(newDateInfo);
        if (directoryService.changeUpdated(id)) {
            return false;
        }

        return dateInfoRepository.existsById(savedDateInfo.getId());
    }

    @Transactional
    public Boolean deleteDateInfoById(Long id) {
        Optional<DateInfo> dateInfo = dateInfoRepository.findById(id);
        if (dateInfo.isEmpty()) {
            log.error("Date info not found");
            return false;
        }
        dateInfoRepository.deleteById(id);
        if (directoryService.changeUpdated(dateInfo.get().getDirectoryId()))
            return false;
        return dateInfoRepository.findById(id).isEmpty();
    }

    @Transactional
    public Boolean updateDateInfoById(Long id, DateInfo dateInfo) {
        Optional<DateInfo> dateInfoOptional = dateInfoRepository.findById(id);
        if (dateInfoOptional.isEmpty()) {
            log.error("Date info not found");
            return false;
        }
        DateInfo newDateInfo = dateInfoOptional.get();
        newDateInfo.setDate(dateInfo.getDate());
        newDateInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        DateInfo savedDateInfo = dateInfoRepository.save(newDateInfo);
        if(directoryService.changeUpdated(newDateInfo.getDirectoryId())){
            return false;
        }
        return savedDateInfo.equals(dateInfo);
    }
}
