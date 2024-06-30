package com.directoryproject.controller;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.Directory;
import com.directoryproject.service.DateInfoService;
import com.directoryproject.service.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "getting the list of all dates from db",
            description = "user send GET method on /date path and app gives list of all created dates notes "
    )
    @GetMapping
    public ResponseEntity<List<DateInfo>> getAllDateInfo() {
        log.info("start getAllDateInfo in DateInfoController");
        return new ResponseEntity<>(dateInfoService.getAllDateInfo(), HttpStatus.OK);
    }

    @Operation(
            summary = "getting the date info with target id",
            description = "user send GET method on /date/{id} and enter the id of target date info," +
                    "application returns this date info if it exists "
    )
    @GetMapping("/{id}")
    public ResponseEntity<DateInfo> getDateInfoById(@PathVariable
                                                        @Parameter(description = "The id of target date info")
                                                        Long id) {
        log.info("start getDateInfoById in DateInfoController");
        Optional<DateInfo> dateInfo = dateInfoService.getDateInfoById(id);
        if (dateInfo.isPresent()) {
            return new ResponseEntity<>(dateInfo.get(), HttpStatus.OK);
        }
        log.error("DateInfo not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "adding a new date note",
            description = "user enters date in body of POST method on /date{id} path and enter id of directory which " +
                    "will contain this date and send it, after that app creates " +
                    "new date note with target directory id in db"
    )
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addDateInfo(@PathVariable
                                                      @Parameter(description = "The id of target directory")
                                                      Long id,
                                                      @Parameter(description = "model of date info")
                                                      @RequestBody DateInfo dateInfo) {
        log.info("start addDateInfo in DateInfoController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("DateInfo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.addDateInfo(id, dateInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "updating date in target date note",
            description = "user send PUT method on /date/{id} path, enters the target date note id and write " +
                    "new date in dody, app updates date in target date note and set Updated time in date info and in " +
                    "directory that contains this date info"
    )
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDateInfoById(@PathVariable
                                                             @Parameter(description = "The id of target date note")
                                                             Long id,
                                                             @Parameter(description = "model of date info")
                                                             @RequestBody DateInfo dateInfo) {
        log.info("start updateDateInfoById in DateInfoController");
        Optional<DateInfo> dateInfoOptional = dateInfoService.getDateInfoById(id);
        if (dateInfoOptional.isEmpty()) {
            log.error("DateInfo not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dateInfoService.updateDateInfoById(id, dateInfo)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "deletes target date info",
            description = "user send DELETE method on /date/{id} enters id of target date info and app deletes " +
                    "it from DB if it exists"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDateInfoById(@PathVariable
                                                             @Parameter(description = "The id of target date note")
                                                             Long id) {
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
