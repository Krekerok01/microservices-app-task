package com.specificgroup.job.util;

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

        public static final String DATA_RECEIVING_EXCEPTION  = "Data receiving problems.";
    }

    /**
     * URL constants for the application.
     */
    public static class UrlPath {

        private UrlPath() {
            throw new UnsupportedOperationException();
        }

        public static final String RAPID_API_URL = "https://jobsearch4.p.rapidapi.com/api/v2/Jobs/Latest";
    }
}