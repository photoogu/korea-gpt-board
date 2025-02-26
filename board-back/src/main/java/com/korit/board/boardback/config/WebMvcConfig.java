package com.korit.board.boardback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${user.dir}")
    private String rootPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        System.out.println(rootPath);   //  >>  C:\korea-gpt\board\board-back 즉 프로젝트 파일 경로
        registry.addResourceHandler("/image/**")                        // /image 로 시작되는 요청이 들어오면
                .addResourceLocations("file:" + rootPath + "/upload/")  // file 로 연결시킴 즉, C:/korea-gpt/board/board-back/upload/ 로 경로를 대체시켜줌
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {   // 파일명이 한글일 수 있으므로 decoding 이 필요
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        resourcePath = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8); // resourcePath 를 UTF-8 로 decoding 해서 다시 넣어줌
                        return super.getResource(resourcePath, location); // getResource 를 호출하면 UTF-8 로 decoding 된 resourcePath 를 호출해줌
                    }
                });
    }
}
