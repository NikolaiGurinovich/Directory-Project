package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.service.DirectoryService;
import com.directoryproject.service.NumberInfoService;
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
@RequestMapping("/number")
public class NumberInfoController {

    private final NumberInfoService numberInfoService;
    private final DirectoryService directoryService;

    @Autowired
    public NumberInfoController(NumberInfoService numberInfoService, DirectoryService directoryService) {
        this.numberInfoService = numberInfoService;
        this.directoryService = directoryService;
    }

    @Operation(
            summary = "getting the list of all numbers from db",
            description = "user send GET method on /number path and app gives list of all created number notes "
    )
    @GetMapping
    public ResponseEntity<List<NumberInfo>> getAllNumberInfo() {
        log.info("start getAllNumberInfo in NumberInfoController");
        return new ResponseEntity<>(numberInfoService.getAllNumberInfo(), HttpStatus.OK);
    }

    @Operation(
            summary = "getting the number info with target id",
            description = "user send GET method on /number/{id} and enter the id of target number info," +
                    "application returns this number info if it exists "
    )
    @GetMapping("/{id}")
    public ResponseEntity<NumberInfo> getNumberInfoById(@PathVariable
                                                            @Parameter(description = "The id of target number info")
                                                            Long id) {
        log.info("start getNumberInfoById in NumberInfoController");
        Optional<NumberInfo> numberInfo = numberInfoService.getNumberInfoById(id);
        if (numberInfo.isPresent()) {
            return new ResponseEntity<>(numberInfo.get(), HttpStatus.OK);
        }
        log.error("Number info not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "adding a new number info",
            description = "user enters number in body of POST method on /number{id} path and enter id of directory which " +
                    "will contain this number and send it, after that app creates " +
                    "new number info with target directory id in db"
    )
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addNumber(@PathVariable
                                                    @Parameter(description = "The id of target directory")
                                                    Long id,
                                                    @Parameter(description = "The model of number info")
                                                    @RequestBody NumberInfo numberInfo) {
        log.info("start addNumber in NumberInfoController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(numberInfoService.addNumber(id, numberInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "updating number in target number info",
            description = "user send PUT method on /number/{id} path, enters the target number info id and write " +
                    "new number in dody, app updates number in target number info and set Updated time in number info and in " +
                    "directory that contains this number info"
    )
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateNumberById(@PathVariable
                                                           @Parameter(description = "The id of target number info")
                                                           Long id,
                                                           @Parameter(description = "The model of number info")
                                                           @RequestBody NumberInfo numberInfo) {
        log.info("start updateNumberById in NumberInfoController");
        Optional<NumberInfo> numberInfoOptional = numberInfoService.getNumberInfoById(id);
        if (numberInfoOptional.isEmpty()) {
            log.error("Number info not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(numberInfoService.updateNumberById(id, numberInfo)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "deletes target number info",
            description = "user send DELETE method on /number/{id} enters id of target number info and app deletes " +
                    "it from DB if it exists"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNumberById(@PathVariable
                                                           @Parameter(description = "The id of target number info")
                                                           Long id) {
        log.info("start deleteNumberById in NumberInfoController");
        Optional<NumberInfo> numberInfo = numberInfoService.getNumberInfoById(id);
        if (numberInfo.isEmpty()) {
            log.error("Number info not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(numberInfoService.deleteNumberById(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
