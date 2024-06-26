package com.directoryproject.repository;

import com.directoryproject.model.DateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateInfoRepository extends JpaRepository<DateInfo,Long> {
    List<DateInfo> findByDirectoryId(Long directoryId);
}
