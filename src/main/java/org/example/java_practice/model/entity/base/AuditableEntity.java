package org.example.java_practice.model.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.example.java_practice.model.converter.DeleteStatusConverter;
import org.example.java_practice.model.converter.LocalDateAttributeConverter;
import org.example.java_practice.model.enumeration.DeleteStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Setter
@Getter
@MappedSuperclass
public class AuditableEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = -3929166636254336754L;

    @Transient
    private final int MY_PAGE_UNKNOWN_USER_ID = 999999;

    @Column(name = "create_user_id")
    private Integer createUserId;

    @Column(name = "update_user_id")
    private Integer updateUserId;

    @Column(name = "create_date", columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDateTime createdDate;

    @Column(name = "update_date", columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDateTime updatedDate;

    @PreUpdate
    private void onUpdate() {
        this.updatedDate = this.getCurrentJapanDate();
        try {
            this.updateUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
        } catch (Exception ex) {
            this.updateUserId = MY_PAGE_UNKNOWN_USER_ID;
        }
    }

    @PrePersist
    private void onSave() {
        this.createdDate = this.getCurrentJapanDate();
        try {
            this.createUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
        } catch (Exception ex) {
            this.createUserId = MY_PAGE_UNKNOWN_USER_ID;
        }
    }

    private LocalDateTime getCurrentJapanDate() {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat japanDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS",
                Locale.JAPAN);
        japanDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String japanDateString = japanDateFormat.format(date);

        SimpleDateFormat systemDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        try {
            date = systemDateFormat.parse(japanDateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }
}
