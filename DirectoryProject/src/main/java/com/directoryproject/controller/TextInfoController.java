package com.directoryproject.controller;

import com.directoryproject.model.Directory;
import com.directoryproject.model.TextInfo;
import com.directoryproject.service.DirectoryService;
import com.directoryproject.service.TextInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<List<TextInfo>> getAllTextInfo() {
        return new ResponseEntity<>(textInfoService.getAllTextInfo(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TextInfo> getTextInfoById(@PathVariable Long id) {
        Optional<TextInfo> textInfo = textInfoService.getTextInfoById(id);
        if (textInfo.isPresent()) {
            return new ResponseEntity<>(textInfo.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addTextInfo(@PathVariable Long id, @RequestBody TextInfo textInfo) {
        Optional<Directory> directory = directoryService.getDirectoryById(id);
        if (directory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(textInfoService.addText(id, textInfo)
                ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTextInfoById(@PathVariable Long id) {
        Optional<TextInfo> textInfo = textInfoService.getTextInfoById(id);
        if (textInfo.isPresent()) {
            return new ResponseEntity<>(textInfoService.deleteTextInfoById(id)
                    ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
