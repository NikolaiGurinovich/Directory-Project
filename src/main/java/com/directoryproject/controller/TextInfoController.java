package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.TextInfo;
import com.directoryproject.service.DirectoryService;
import com.directoryproject.service.TextInfoService;
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
@RequestMapping("/text")
public class TextInfoController {

    private final TextInfoService textInfoService;
    private final DirectoryService directoryService;

    @Autowired
    public TextInfoController(TextInfoService textInfoService, DirectoryService directoryService) {
        this.textInfoService = textInfoService;
        this.directoryService = directoryService;
    }

    @Operation(
            summary = "getting the list of all text infos from db",
            description = "user send GET method on /text path and app gives list of all created text notes "
    )
    @GetMapping
    public ResponseEntity<List<TextInfo>> getAllTextInfo() {
        log.info("start getAllTextInfo in TextInfoController");
        return new ResponseEntity<>(textInfoService.getAllTextInfo(), HttpStatus.OK);
    }

    @Operation(
            summary = "getting the text info with target id",
            description = "user send GET method on /text/{id} and enter the id of target text info," +
                    "application returns this text info if it exists "
    )
    @GetMapping("/{id}")
    public ResponseEntity<TextInfo> getTextInfoById(@PathVariable
                                                        @Parameter(description = "The id of target text info")
                                                        Long id) {
        log.info("start getTextInfoById in TextInfoController");
        Optional<TextInfo> textInfo = textInfoService.getTextInfoById(id);
        if (textInfo.isPresent()) {
            return new ResponseEntity<>(textInfo.get(), HttpStatus.OK);
        }
        log.error("Text info not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "adding a new text note",
            description = "user enters text in body of POST method on /text{id} path and enter id of directory which " +
                    "will contain this text and send it, after that app creates " +
                    "new text note with target directory id in db"
    )
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addTextInfo(@PathVariable
                                                      @Parameter(description = "The id of target directory")
                                                      Long id,
                                                      @Parameter(description = "model of text info")
                                                      @RequestBody TextInfo textInfo) {
        log.info("start addTextInfo in TextInfoController");
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(textInfoService.addText(id, textInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "updating text in target text note",
            description = "user send PUT method on /text/{id} path, enters the target text note id and write " +
                    "new text in dody, app updates text in target text note and set Updated time in text info and in " +
                    "directory that contains this text info"
    )
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateTextInfoById(@PathVariable
                                                             @Parameter(description = "The id of target text info")
                                                             Long id,
                                                             @Parameter(description = "model of text info")
                                                             @RequestBody TextInfo textInfo) {
        log.info("start updateTextInfoById in TextInfoController");
        Optional<TextInfo> textInfoOptional = textInfoService.getTextInfoById(id);
        if (textInfoOptional.isEmpty()) {
            log.error("Text info not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(textInfoService.updateTextInfoById(id, textInfo)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @Operation(
            summary = "deletes target text info",
            description = "user send DELETE method on /text/{id} enters id of target text info and app deletes " +
                    "it from DB if it exists"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTextInfoById(@PathVariable
                                                             @Parameter(description = "The id of target text info")
                                                             Long id) {
        log.info("start deleteTextInfoById in TextInfoController");
        Optional<TextInfo> textInfo = textInfoService.getTextInfoById(id);
        if (textInfo.isPresent()) {
            return new ResponseEntity<>(textInfoService.deleteTextInfoById(id)
                    ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
        }
        log.error("Text info not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
