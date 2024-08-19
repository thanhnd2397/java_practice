package org.example.java_practice.model.converter;

import jakarta.persistence.AttributeConverter;
import org.example.java_practice.model.enumeration.DeleteStatus;

/**
 * Converter for {@link DeleteStatus} and String column in database
 */
public class DeleteStatusConverter implements AttributeConverter<DeleteStatus, String> {

    @Override
    public String convertToDatabaseColumn(DeleteStatus deleteStatus) {
        return deleteStatus.getValue();
    }

    @Override
    public DeleteStatus convertToEntityAttribute(String value) {
        return DeleteStatus.fromValue(value);
    }
}
