package com.specificgroup.news.service;

import com.specificgroup.news.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {
    List<NewsResponse> findCurrentNews();
}
