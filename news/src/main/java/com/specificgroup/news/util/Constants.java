package com.specificgroup.news.util;

/**
 * Application constants.
 */
public class Constants {
    /**
     * Messages constants for the application.
     */
    public static class Message {

        private Message() {
            throw new UnsupportedOperationException();
        }

        public static final String DATA_PROCESSING_EXCEPTION = "Data processing problems.";
        public static final String SERVICE_UNAVAILABLE_EXCEPTION  = "Service unavailable. Data receiving problems.";
    }

    /**
     * URL constants for the application.
     */
    public static class UrlPath {

        private UrlPath() {
            throw new UnsupportedOperationException();
        }

        public static final String RAPID_API_URL = "https://newsi-api.p.rapidapi.com/api/category?category=science_and_technology&language=en&country=us&sort=top&page=1&limit=10";
    }
}