package com.korit.board.boardback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class FileService {

    @Value("${user.dir}")
    private String rootPath;

    public String saveFile(String path, MultipartFile file) {
        if(file.isEmpty()) {
            return null;
        }

        String newFilename = null;  // 사용자들끼리의 이미지 파일 이름이 겹칠 수 있으므로 새로운 이름을 부여해야함
        try {
            String originFilename = file.getOriginalFilename();
            newFilename = UUID.randomUUID().toString().replaceAll("-", "") + "_" + originFilename; // UUID 는 유일한 키값을 제공해줌 (일반 랜덤 함수도 유일한 키값이 아님)
            File newFilePath = new File(rootPath + "/" + path);
            if(!newFilePath.exists()) {
                newFilePath.mkdirs(); // mkdirs: 중간에 경로가 비었으면 전체 경로를 모두 만들어줌, mkdir: 중간에 경로가 비어있으면 경로 만들어주지 못함
            }
            File newFile = new File(rootPath + "/" + path + "/" + newFilename); // 경로 설정(path : 이미지 파일 구분(게시글 이미지인지, 프로필 이미지인지))
            file.transferTo(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFilename;
    }

    public void deleteFile(String path) {
        File file = new File(rootPath + "/" + path);
        if(file.exists()) {
            file.delete();
        }
    }
}
