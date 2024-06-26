package com.directoryproject.repository;

import com.directoryproject.model.TextInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextInfoRepository extends JpaRepository<TextInfo, Long> {
    List<TextInfo> findByDirectoryId(Long directoryId);
}
