package com.example.medium.domain.file.repository;

import com.example.medium.domain.file.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

}
