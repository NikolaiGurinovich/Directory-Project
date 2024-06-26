package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.service.DirectoryService;
import com.directoryproject.service.NumberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<List<NumberInfo>> getAllNumberInfo() {
        return new ResponseEntity<>(numberInfoService.getAllNumberInfo(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NumberInfo> getNumberInfoById(@PathVariable Long id) {
        Optional<NumberInfo> numberInfo = numberInfoService.getNumberInfoById(id);
        if (numberInfo.isPresent()) {
            return new ResponseEntity<>(numberInfo.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addNumber(@PathVariable Long id, @RequestBody NumberInfo numberInfo) {
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(numberInfoService.addNumber(id, numberInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNumberById(@PathVariable Long id) {
        Optional<NumberInfo> numberInfo = numberInfoService.getNumberInfoById(id);
        if (numberInfo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(numberInfoService.deleteNumberById(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
