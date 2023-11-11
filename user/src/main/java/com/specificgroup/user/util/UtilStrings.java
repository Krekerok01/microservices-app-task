package com.specificgroup.user.util;

public class UtilStrings {
    private static final String USER_WAS_SUCCESSFULLY = "User with id %d was successfully";
    private static final String DELETED = "Deleted";
    private static final String UPDATED = "Updated";

    public static String userWasSuccessfullyModified(long id, Action action) {
        String actionString = switch (action) {
            case DELETED -> DELETED;
            case UPDATED -> UPDATED;
        };

        return String.format("%s %s", USER_WAS_SUCCESSFULLY.formatted(id), actionString);
    }

    public enum Action {
        UPDATED,
        DELETED
    }
}
