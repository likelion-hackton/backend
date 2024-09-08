package com.example.backend.image.service;

import com.example.backend.image.entity.BaseImage;
import com.example.backend.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
@Transactional
@Primary
@RequiredArgsConstructor
public class ImageService {

    private final StorageService storageService;

    public <T extends BaseImage, E> void procesAndAddImages(E entity, List<MultipartFile> imageFiles,
                                                            Function<String, T> imageCreator, BiConsumer<E, T> imageAdder){

        for (MultipartFile imageFile : imageFiles) {
            // 디렉토리에 저장 후 Url 받아오기
            String imageUrl = storageService.storeFile(imageFile);
            // url 설정
            T image = imageCreator.apply(imageUrl);
            // 추가
            imageAdder.accept(entity, image);
        }
    }
}
