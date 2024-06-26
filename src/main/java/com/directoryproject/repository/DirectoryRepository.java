package com.directoryproject.repository;

import com.directoryproject.model.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    Optional<Directory> findByDirectoryName(String directoryName);
}
