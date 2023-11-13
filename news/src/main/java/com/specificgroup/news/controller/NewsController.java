package com.specificgroup.news.controller;

import com.specificgroup.news.dto.response.NewsResponse;
import com.specificgroup.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public List<NewsResponse> findCurrentNews(){
        return newsService.findCurrentNews();
    }
}
