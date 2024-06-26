package com.directoryproject.repository;

import com.directoryproject.model.NumberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumberInfoRepository extends JpaRepository<NumberInfo, Long> {
    List<NumberInfo> findByDirectoryId(Long id);
}
