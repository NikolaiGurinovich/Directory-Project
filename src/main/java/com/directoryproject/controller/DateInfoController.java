package com.directoryproject.controller;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.Directory;
import com.directoryproject.service.DateInfoService;
import com.directoryproject.service.DirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/date")
public class DateInfoController {

    private final DateInfoService dateInfoService;
    private final DirectoryService directoryService;

    @Autowired
    public DateInfoController(DateInfoService dateInfoService, DirectoryService directoryService) {
        this.dateInfoService = dateInfoService;
        this.directoryService = directoryService;
    }

    @GetMapping
    public ResponseEntity<List<DateInfo>> getAllDateInfo() {
        log.info("start getAllDateInfo in DateInfoController");
        return new ResponseEntity<>(dateInfoService.getAllDateInfo(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DateInfo> getDateInfoById(@PathVariable Long id) {
        log.info("start getDateInfoById in DateInfoController");
        Optional<DateInfo> dateInfo = dateInfoService.getDateInfoById(id);
        if (dateInfo.isPresent()) {
            return new ResponseEntity<>(dateInfo.get(), HttpStatus.OK);
        }
        log.error("DateInfo not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addDateInfo(@PathVariable Long id, @RequestBody DateInfo dateInfo) {
        log.info("start addDateInfo in DateInfoController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("DateInfo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.addDateInfo(id, dateInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDateInfoById(@PathVariable Long id, @RequestBody DateInfo dateInfo) {
        log.info("start updateDateInfoById in DateInfoController");
        Optional<DateInfo> dateInfoOptional = dateInfoService.getDateInfoById(id);
        if (dateInfoOptional.isEmpty()) {
            log.error("DateInfo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.updateDateInfoById(id, dateInfo)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDateInfoById(@PathVariable Long id) {
        log.info("start deleteDateInfoById in DateInfoController");
        Optional<DateInfo> dateInfo = dateInfoService.getDateInfoById(id);
        if (dateInfo.isEmpty()) {
            log.error("DateInfo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.deleteDateInfoById(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
