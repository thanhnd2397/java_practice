package org.example.java_practice.model.enumeration;

import lombok.Getter;
/**
 * Enum for Delete Status
 */
@Getter
public enum DeleteStatus {
    /**
     * Entity status: ACTIVE
     */
    ACTIVE("0"),

    /**
     * Entity status: DELETED
     */
    DELETED("1"),
    ;
    private final String value;

    /**
     * Default constructor
     *
     * @param value column value
     */
    DeleteStatus(String value) {
        this.value = value;
    }

    /**
     * Retrieve a DeleteStatus instance by given the string value.
     *
     * @param value String value "0", "1"
     * @return the DeleteStatus associated with given value, otherwise return null
     */
    public static DeleteStatus fromValue(String value) {
        for (DeleteStatus deleteStatus : values()) {
            if (deleteStatus.value.equals(value)) {
                return deleteStatus;
            }
        }
        return null;
    }
}
