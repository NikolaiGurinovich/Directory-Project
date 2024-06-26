package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.dto.CreateDirectoryDto;
import com.directoryproject.model.dto.GetDirectoryInfoDto;
import com.directoryproject.model.dto.UpdateDirectoryDto;
import com.directoryproject.service.DirectoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/directory")
public class DirectoryController {

    private final DirectoryService directoryService;

    @Autowired
    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createDirectory (@RequestBody @Valid CreateDirectoryDto createDirectoryDto,
                                                       BindingResult bindingResult){
        log.info("Start createDirectory in DirectoryController");
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(directoryService.createDirectory(createDirectoryDto)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping
    public ResponseEntity<List<Directory>> getAllDirectoryList (){
        log.info("Start getAllDirectoryList in DirectoryController");
       return new ResponseEntity<>(directoryService.getAllDirectoryList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetDirectoryInfoDto> getDirectoryInfoById(@PathVariable Long id){
        log.info("Start getDirectoryListById in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isPresent()) {
            return new ResponseEntity<>(directoryService.getDirectoryInfoById(id).get(), HttpStatus.OK);
        }
        log.error("Directory not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDirectoryBuId(@PathVariable Long id,
                                                          @RequestBody UpdateDirectoryDto updateDirectoryDto){
        log.info("Start updateDirectoryBuId in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.updateDirectoryById(id, updateDirectoryDto)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/invalid/{id}")
    public ResponseEntity<HttpStatus> updateDirectoryInvalid(@PathVariable Long id){
        log.info("Start updateDirectoryInvalid in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.updateDirectoryInvalid(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/valid/{id}")
    public ResponseEntity<HttpStatus> updateDirectoryValid(@PathVariable Long id){
        log.info("Start updateDirectoryValid in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.updateDirectoryValid(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDirectory(@PathVariable Long id){
        log.info("Start deleteDirectory in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.deleteDirectoryById(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
