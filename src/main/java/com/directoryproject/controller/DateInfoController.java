package com.directoryproject.controller;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.Directory;
import com.directoryproject.service.DateInfoService;
import com.directoryproject.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        return new ResponseEntity<>(dateInfoService.getAllDateInfo(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DateInfo> getDateInfoById(@PathVariable Long id) {
        Optional<DateInfo> dateInfo = dateInfoService.getDateInfoById(id);
        if (dateInfo.isPresent()) {
            return new ResponseEntity<>(dateInfo.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addDateInfo(@PathVariable Long id, @RequestBody DateInfo dateInfo) {
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.addDateInfo(id, dateInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDateInfoById(@PathVariable Long id) {
        Optional<DateInfo> dateInfo = dateInfoService.getDateInfoById(id);
        if (dateInfo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.deleteDateInfoById(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
