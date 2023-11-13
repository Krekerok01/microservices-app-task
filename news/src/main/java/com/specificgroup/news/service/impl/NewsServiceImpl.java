package com.specificgroup.news.service.impl;

import com.specificgroup.news.dto.response.NewsResponse;
import com.specificgroup.news.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Override
    public List<NewsResponse> findCurrentNews() {
        return null;
    }
}
