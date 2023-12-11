package com.specificgroup.blog.util;

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

        public static final String SERVICE_REQUEST_SUCCESSFULLY_PROCESSED = "Request successfully processed.";
        public static final String RESOURCE_NOT_FOUND = "%s not found.";
        public static final String ACCESS_DENIED = "Access denied.";
        public static final String EXPIRED_TOKEN = "Token has expired.";
        public static final String ARGUMENT_VALIDATION_FAILED = "Argument validation failed.";
        public static final String UNAVAILABLE_SERVICE = "%s service is unavailable. Try again later.";
    }

    /**
     * URL constants for the application.
     */
    public static class UrlPath {

        private UrlPath() {
            throw new UnsupportedOperationException();
        }

        public static final String GET_SUBSCRIPTIONS_IDS_URL = "/api/v1/subscriptions/subscriber?userSubscriberId=";
    }
}