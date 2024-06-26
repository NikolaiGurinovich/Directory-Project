package com.directoryproject.service;

import com.directoryproject.model.DateInfo;
import com.directoryproject.model.Directory;
import com.directoryproject.model.NumberInfo;
import com.directoryproject.model.TextInfo;
import com.directoryproject.model.dto.CreateDirectoryDto;
import com.directoryproject.model.dto.GetDirectoryInfoDto;
import com.directoryproject.model.dto.UpdateDirectoryDto;
import com.directoryproject.repository.DateInfoRepository;
import com.directoryproject.repository.DirectoryRepository;
import com.directoryproject.repository.NumberInfoRepository;
import com.directoryproject.repository.TextInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final TextInfoRepository textInfoRepository;
    private final DateInfoRepository dateInfoRepository;
    private final NumberInfoRepository numberInfoRepository;

    @Autowired
    public DirectoryService(DirectoryRepository directoryRepository, TextInfoRepository textInfoRepository,
                            DateInfoRepository dateInfoRepository, NumberInfoRepository numberInfoRepository) {
        this.directoryRepository = directoryRepository;
        this.textInfoRepository = textInfoRepository;
        this.dateInfoRepository = dateInfoRepository;
        this.numberInfoRepository = numberInfoRepository;
    }

    public Boolean createDirectory(CreateDirectoryDto createDirectoryDto) {
        Directory directory = new Directory();
        if (createDirectoryDto.getDirectoryName() != null &&
                directoryRepository.findByDirectoryName(createDirectoryDto.getDirectoryName()).isEmpty() ) {
            directory.setDirectoryName(createDirectoryDto.getDirectoryName());
            directory.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            directory.setIsValid(true);
        } else return false;
        Directory createdDirectory = directoryRepository.save(directory);
        if (createDirectoryDto.getText() != null && !createDirectoryDto.getText().isBlank()) {
            TextInfo textInfo = new TextInfo();
            textInfo.setText(createDirectoryDto.getText());
            textInfo.setDirectoryId(directory.getId());
            textInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            textInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            textInfoRepository.save(textInfo);
        }
        if(createDirectoryDto.getNumber() != null){
            NumberInfo numberInfo = new NumberInfo();
            numberInfo.setNumber(createDirectoryDto.getNumber());
            numberInfo.setDirectoryId(createdDirectory.getId());
            numberInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            numberInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            numberInfoRepository.save(numberInfo);
        }
        if(createDirectoryDto.getDate() != null) {
            DateInfo dateInfo = new DateInfo();
            dateInfo.setDate(createDirectoryDto.getDate());
            dateInfo.setDirectoryId(createdDirectory.getId());
            dateInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            dateInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            dateInfoRepository.save(dateInfo);
        }
        return getDirectoryById(createdDirectory.getId()).isPresent();
    }

    public Optional<Directory> getDirectoryById(Long id) {
        return directoryRepository.findById(id);
    }

    public List<Directory> getAllDirectoryList() {
        return directoryRepository.findAll();
    }

    public Optional<GetDirectoryInfoDto> getDirectoryInfoById(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            return Optional.empty();
        }
        GetDirectoryInfoDto directoryInfoDto = new GetDirectoryInfoDto();
        directoryInfoDto.setId(id);
        directoryInfoDto.setDirectoryName(directoryOptional.get().getDirectoryName());
        if(!textInfoRepository.findByDirectoryId(id).isEmpty()){
            directoryInfoDto.setTextInfo(this.textInfoRepository.findByDirectoryId(id));
        }
        if(!numberInfoRepository.findByDirectoryId(id).isEmpty()){
            directoryInfoDto.setNumberInfo(this.numberInfoRepository.findByDirectoryId(id));
        }
        if(!dateInfoRepository.findByDirectoryId(id).isEmpty()){
            directoryInfoDto.setDateInfo(this.dateInfoRepository.findByDirectoryId(id));
        }
        directoryInfoDto.setCreated(directoryOptional.get().getCreated());
        directoryInfoDto.setUpdated(directoryOptional.get().getUpdated());
        return Optional.of(directoryInfoDto);
    }

    public Boolean updateDirectoryById(Long id, UpdateDirectoryDto updateDirectoryDto) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            return false;
        }
        Directory directory = directoryOptional.get();
        if (updateDirectoryDto.getDirectoryName() != null) {
            directory.setDirectoryName(updateDirectoryDto.getDirectoryName());
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        }
        if (updateDirectoryDto.getIsValid() != null){
            directory.setIsValid(updateDirectoryDto.getIsValid());
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        }
        if (updateDirectoryDto.getNumberId() != null){
            Optional<NumberInfo> numberInfoOptional = numberInfoRepository.findById(updateDirectoryDto.getNumberId());
            if (numberInfoOptional.isEmpty()) {
                return false;
            }
            NumberInfo numberInfo = numberInfoOptional.get();
            if(updateDirectoryDto.getNumber() != null){
                numberInfo.setNumber(updateDirectoryDto.getNumber());
                numberInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                numberInfoRepository.save(numberInfo);
            }
        }
        if (updateDirectoryDto.getDateId() != null){
            Optional<DateInfo> dateInfoOptional = dateInfoRepository.findById(updateDirectoryDto.getDateId());
            if (dateInfoOptional.isEmpty()) {
                return false;
            }
            DateInfo dateInfo = dateInfoOptional.get();
            if(updateDirectoryDto.getDate() != null){
                dateInfo.setDate(updateDirectoryDto.getDate());
                dateInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                dateInfoRepository.save(dateInfo);
            }
        }
        if(updateDirectoryDto.getTextId() != null){
            Optional<TextInfo> textInfoOptional = textInfoRepository.findById(updateDirectoryDto.getTextId());
            if (textInfoOptional.isEmpty()) {
                return false;
            }
            TextInfo textInfo = textInfoOptional.get();
            if(updateDirectoryDto.getText() != null){
                textInfo.setText(updateDirectoryDto.getText());
                textInfo.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                textInfoRepository.save(textInfo);
            }
        }
        Directory savedDirectory = directoryRepository.save(directory);
        return savedDirectory.equals(directoryOptional.get());
    }

    public Boolean deleteDirectoryById(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            return false;
        }
        directoryRepository.delete(directoryOptional.get());
        return getDirectoryById(id).isEmpty();
    }

    public Boolean updateDirectoryInvalid(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            return false;
        }
        Directory directory = directoryOptional.get();
        if(directory.getIsValid()){
            directory.setIsValid(Boolean.FALSE);
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Directory savedDirectory = directoryRepository.save(directory);
            return savedDirectory.equals(directoryOptional.get());
        }
        return false;
    }

    public Boolean updateDirectoryValid(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            return false;
        }
        Directory directory = directoryOptional.get();
        if(!directory.getIsValid()){
            directory.setIsValid(Boolean.TRUE);
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Directory savedDirectory = directoryRepository.save(directory);
            return savedDirectory.equals(directoryOptional.get());
        }
        return false;
    }
}
