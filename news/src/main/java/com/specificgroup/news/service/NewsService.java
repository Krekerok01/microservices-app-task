package com.specificgroup.news.service;

import com.specificgroup.news.dto.NewsResponse;

import java.util.List;

public interface NewsService {

    /**
     * Load a list of news from a third-party service
     *
     * @return a list of NewsResponse objects
     */
    List<NewsResponse> findCurrentNews();
}
