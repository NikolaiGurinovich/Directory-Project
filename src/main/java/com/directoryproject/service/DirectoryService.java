package com.directoryproject.service;

import com.directoryproject.model.Directory;
import com.directoryproject.model.dto.GetDirectoryInfoDto;
import com.directoryproject.repository.DateInfoRepository;
import com.directoryproject.repository.DirectoryRepository;
import com.directoryproject.repository.NumberInfoRepository;
import com.directoryproject.repository.TextInfoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Transactional
    public Boolean createDirectory(Directory directory) {
        Directory newDirectory = new Directory();
        if (directory.getDirectoryName() != null &&
                directoryRepository.findByDirectoryName(directory.getDirectoryName()).isEmpty() ) {
            newDirectory.setDirectoryName(directory.getDirectoryName());
            newDirectory.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            newDirectory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            newDirectory.setIsValid(true);
        } else {
            log.error("directory name is invalid or duplicated");
            return false;
        }
        Directory createdDirectory = directoryRepository.save(newDirectory);
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
            log.error("directory not found");
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

    @Transactional
    public Boolean updateDirectoryById(Long id, Directory directory) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            log.error("newDirectory not found");
            return false;
        }
        Directory newDirectory = directoryOptional.get();
        if (directory.getDirectoryName() != null) {
            newDirectory.setDirectoryName(directory.getDirectoryName());
            newDirectory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        }
        if (directory.getIsValid() != null){
            newDirectory.setIsValid(directory.getIsValid());
            newDirectory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        }
        Directory savedDirectory = directoryRepository.save(newDirectory);
        return savedDirectory.equals(directoryOptional.get());
    }

    public Boolean deleteDirectoryById(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            log.error("directory not found");
            return false;
        }
        directoryRepository.delete(directoryOptional.get());
        return getDirectoryById(id).isEmpty();
    }

    public Boolean updateDirectoryInvalid(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            log.error("directory not found");
            return false;
        }
        Directory directory = directoryOptional.get();
        if(directory.getIsValid()){
            directory.setIsValid(Boolean.FALSE);
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Directory savedDirectory = directoryRepository.save(directory);
            return savedDirectory.equals(directoryOptional.get());
        }
        log.error("directory is already invalid");
        return false;
    }

    public Boolean updateDirectoryValid(Long id) {
        Optional<Directory> directoryOptional = directoryRepository.findById(id);
        if (directoryOptional.isEmpty()) {
            log.error("directory not found");
            return false;
        }
        Directory directory = directoryOptional.get();
        if(!directory.getIsValid()){
            directory.setIsValid(Boolean.TRUE);
            directory.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            Directory savedDirectory = directoryRepository.save(directory);
            return savedDirectory.equals(directoryOptional.get());
        }
        log.error("directory is already  valid");
        return false;
    }

    public boolean changeUpdated(Long id) {
        Optional<Directory> directory = getDirectoryById(id);
        if (directory.isEmpty()) {
            log.error("Directory not found");
            return true;
        }
        directory.get().setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        directoryRepository.save(directory.get());
        return false;
    }
}
