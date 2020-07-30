package com.gjm.file_cloud.dao;

import com.gjm.file_cloud.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FileDao extends JpaRepository<File, Long> {
    @Query("SELECT f FROM File f WHERE f.owner.username = :username")
    Page<File> findFilesByOwnerName(@Param("username") String username, Pageable pageable);
}
