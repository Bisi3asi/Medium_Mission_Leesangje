package com.example.medium.domain.file.service;

import com.example.medium.domain.file.entity.ImageFile;
import com.example.medium.domain.file.repository.ImageFileRepository;
import com.example.medium.domain.post.entity.Post;
import com.example.medium.global.dto.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Getter
public class ImageFileService {
    private final ImageFileRepository imageFileRepository;
    // 프로젝트 루트 디렉토리 / 하위 폴더 images에 이미지 저장
    private final String FILESTORE_PATH = System.getProperty("user.dir") +"/images/";

    @Transactional
    public ResponseDto<ImageFile> create(MultipartFile multipartFile, Post post) {
        String storeFilename = storeAndGetFilename(multipartFile);
        ImageFile imageFile = ImageFile.builder()
                .filename(storeFilename)
                .filesize(multipartFile.getSize())
                .post(post)
                .build();
        imageFileRepository.save(imageFile);

        return ResponseDto.of("200", "Image has been successfully uploaded", imageFile);
    }

    public ImageFile get(Long id){
        return imageFileRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        );
    }

    @SneakyThrows
    public String storeAndGetFilename(MultipartFile multipartFile) {
        if (getFileType(multipartFile).contentEquals("")) {
            throw new HttpMediaTypeNotSupportedException("Unsupported Image File type");
        }

        // ex) <randomUUID> + .jpg
        String storeFilename = setUniqueFilename(getFileType(multipartFile));

        // ex) C:/mediumimagfiles/<RandomUUID>.jpg 의 경로로 저장
        File file = new File(FILESTORE_PATH + storeFilename);

        // 파일 경로가 없을 시 이미지 파일 생성
        if (!file.exists()) {
            if (!file.mkdirs()){
                throw new IOException("Failed to create directory");
            }
        }

        // 파일 저장
        multipartFile.transferTo(new File(FILESTORE_PATH + storeFilename));

        return storeFilename;
    }

    @SneakyThrows
    private String getFileType(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (contentType != null) {
            MediaType mediaType = MediaType.parseMediaType(contentType);
            switch (mediaType.toString()) {
                case MediaType.IMAGE_JPEG_VALUE -> {
                    return ".jpeg";
                }
                case MediaType.IMAGE_PNG_VALUE -> {
                    return ".png";
                }
                case MediaType.IMAGE_GIF_VALUE -> {
                    return ".gif";
                }
            }
        }
        return "";
    }

    @SneakyThrows
    private String setUniqueFilename(String fileType) {
        // ex) randomUUID + .jpg
        return UUID.randomUUID() + fileType;
    }
}
