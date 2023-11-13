package com.specificgroup.news.service;

import com.specificgroup.news.dto.NewsResponse;

import java.util.List;

public interface NewsService {
    List<NewsResponse> findCurrentNews();
}
