package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.dto.GetDirectoryInfoDto;
import com.directoryproject.service.DirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "creation of new directory",
            description = "user enters directory name in body of POST method on /directory path and send it," +
                    "after that app creates new directory in db"
    )
    @PostMapping
    public ResponseEntity<HttpStatus> createDirectory (@RequestBody @Valid
                                                           @Parameter(description = "Directory model")
                                                           Directory directory, BindingResult bindingResult){
        log.info("Start createDirectory in DirectoryController");
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(directoryService.createDirectory(directory)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "getting the list of all directories",
            description = "user send GET method on /directory path and app gives list of all created directories "
    )
    @GetMapping
    public ResponseEntity<List<Directory>> getAllDirectoryList (){
        log.info("Start getAllDirectoryList in DirectoryController");
       return new ResponseEntity<>(directoryService.getAllDirectoryList(), HttpStatus.OK);
    }

    @Operation(
            summary = "getting the directory with target id",
            description = "user send GET method on /directory/{id} and enter the id of target directory," +
                    "application returns this directory if it exists and all notes that this directory has "
    )
    @GetMapping("/{id}")
    public ResponseEntity<GetDirectoryInfoDto> getDirectoryInfoById(@PathVariable
                                                                        @Parameter(description = "The id of target directory")
                                                                        Long id){
        log.info("Start getDirectoryListById in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isPresent()) {
            return new ResponseEntity<>(directoryService.getDirectoryInfoById(id).get(), HttpStatus.OK);
        }
        log.error("Directory not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "updating name of target directory",
            description = "user send PUT method on /directory/{id} path, enters the target directory id and write " +
                    "new directory name in dody, app updates name of target directory and set Updated time"
    )
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDirectoryById(@PathVariable
                                                              @Parameter(description = "The id of target directory")
                                                              Long id,
                                                          @Parameter(description = "Model with new name")
                                                          @RequestBody @Valid Directory directory){
        log.info("Start updateDirectoryBuId in DirectoryController");
        Optional<Directory> directoryOptional = directoryService.getDirectoryById(id);
        if (directoryOptional.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.updateDirectoryById(id, directory)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "changes isValid variable to false",
            description = "user send PUT method on /directory/invalid/{id} enters id of target directory and app changes " +
                    "isValid variable to false if it isn't false already"
    )
    @PutMapping("/invalid/{id}")
    public ResponseEntity<HttpStatus> updateDirectoryInvalid(@PathVariable
                                                                 @Parameter(description = "The id of target directory")
                                                                 Long id){
        log.info("Start updateDirectoryInvalid in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.updateDirectoryInvalid(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "changes isValid variable to true",
            description = "user send PUT method on /directory/valid/{id} enters id of target directory and app changes " +
                    "isValid variable to true if it isn't true already"
    )
    @PutMapping("/valid/{id}")
    public ResponseEntity<HttpStatus> updateDirectoryValid(@PathVariable
                                                               @Parameter(description = "The id of target directory")
                                                               Long id){
        log.info("Start updateDirectoryValid in DirectoryController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directoryService.updateDirectoryValid(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "deletes target directory",
            description = "user send DELETE method on /directory/{id} enters id of target directory and app deletes " +
                    "it from DB if it exists"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDirectory(@PathVariable
                                                          @Parameter(description = "The id of target directory")
                                                          Long id){
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
