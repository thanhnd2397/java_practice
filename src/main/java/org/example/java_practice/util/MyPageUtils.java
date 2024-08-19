package org.example.java_practice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.java_practice.model.dto.ErrorModel;
import org.example.java_practice.model.dto.RestResponse;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyPageUtils {
    private static final Logger logger = LogManager.getLogger(MyPageUtils.class);
    private static MyPageUtils instance;

    private final char[] hexArray = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    private MyPageUtils() {
    }

    public static MyPageUtils getInstance() {
        if (null == instance) {
            instance = new MyPageUtils();
        }
        return instance;
    }

    /**
     * Get Current Japan datetime.
     *
     * @return datetime {@link LocalDateTime}
     */
    public LocalDateTime getCurrentJapanDate() {
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
                cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
    }

    /**
     * Get Current UTC datetime.
     *
     * @return datetime {@link LocalDateTime}
     */
    public LocalDateTime getCurrentUTCDate() {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat japanDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS",
                Locale.JAPAN);
        japanDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
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
                cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
    }

    public LocalDateTime convertFromUtcToJapanDateTime(LocalDateTime utcDateTime) {
        ZonedDateTime utcZoned = ZonedDateTime.of(utcDateTime, ZoneOffset.UTC);
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        return utcZoned.withZoneSameInstant(tokyoZone).toLocalDateTime();
    }

    public LocalDateTime convertFromJapanDateTimeToUtcDateTime(LocalDateTime japanDateTime) {
        ZonedDateTime japan = japanDateTime.atZone(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime utcZoned = japan.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime();
    }

    public String convertLocalDatetimeToDateString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        if (null == dateTime) {
            return "";
        }
        return dateTime.format(formatter);
    }

    public String convertLocalDatetimeToDateTimeString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
        if (null == dateTime) {
            return "";
        }
        return dateTime.format(formatter);
    }

    public String convertLocalDatetimeToStringMonthDay(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.MONTHDAY_FORMAT);
        if (null == dateTime) {
            return "";
        }
        return dateTime.format(formatter);
    }

    public String convertLocalDatetimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                Constants.DATE_FORMAT_TO_STRING);
        if (null == dateTime) {
            return "";
        }
        return dateTime.format(formatter);
    }

    public LocalDateTime convertStringDateFormatToLocalDatetime(String date, boolean isStartDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        LocalDate temp = LocalDate.parse(date, formatter);
        if (isStartDate) {
            return temp.atStartOfDay();
        } else {
            return temp.atTime(23, 59, 59, 999);
        }
    }


    public LocalDateTime convertStringLocalDateFormatToLocalDateTime(String date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);
        return LocalDateTime.parse(date, formatter);
    }

    public void copyNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        List<String> emptyNames = Arrays.stream(pds)
                .map(FeatureDescriptor::getName)
                .filter(name -> null == src.getPropertyValue(name))
                .toList();

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * Create Fail response for failed bean validations
     *
     * @param errorModels {@link ErrorModel}
     */
    public RestResponse createFailResponse(String serviceName, List<ErrorModel> errorModels) {
        RestResponse response = new RestResponse(serviceName);
        response.setSuccess(false);
        response.setError(errorModels);
        return response;
    }

    /**
     * Create Successful response for API
     *
     * @param names field name in json {@link String}
     * @param datas field value in json {@link Object}
     * @return RestResponse {@link RestResponse}
     */
    public RestResponse createSuccessResponse(String serviceName, String[] names, Object... datas) {
        if (names.length != datas.length || names.length == 0) {
            throw new ServiceException("inputs parameter is not correct !");
        }

        RestResponse response = new RestResponse(serviceName);
        Map<String, Object> data = new LinkedHashMap<>();
        response.setData(data);
        response.setSuccess(true);
        for (int i = 0; i < names.length; i++) {
            response.addAttribute(names[i], datas[i]);
        }
        return response;
    }

    public <T> RestResponse createSuccessCompleteFutureResponse(String serviceName, String names,
                                                             CompletableFuture<T> completableFuture)
            throws InterruptedException, ExecutionException {
        RestResponse response = new RestResponse(serviceName);
        Map<String, Object> data = new LinkedHashMap<>();
        response.setData(data);
        response.setSuccess(true);
        response.addAttribute(names, completableFuture.get());
        return response;
    }

    public RestResponse createFailResponse(String serviceName, String messageId,
                                           String messageContent) {
        ErrorModel model = new ErrorModel(messageId, messageContent);
        List<ErrorModel> errorModels = new ArrayList<>();
        errorModels.add(model);
        return this.createFailResponse(serviceName, errorModels);
    }

    /**
     * return a json message when creating jwt failed or jwt is invalid
     *
     * @param httpServletResponse {@link HttpServletResponse}
     * @param httpCode            {@link HttpServletResponse}
     * @param messageId           {@link String}
     * @param messageContent      {@link String}
     */
    public void createFailResponse(HttpServletResponse httpServletResponse, int httpCode,
                                   String messageId, String messageContent, String serviceName) {
        RestResponse restResponse = this.createFailResponse(serviceName, messageId, messageContent);
        httpServletResponse.setContentType(Constants.JSON_TYPE);
        httpServletResponse.setStatus(httpCode);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            String json = this.convertToJson(restResponse);
            httpServletResponse.getWriter().write(json);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String convertToJson(RestResponse api) throws JsonProcessingException {
        if (api == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(api);
    }

    /**
     * Calculate MD5 string of given input string.
     *
     * @param input string need to calculate md5
     * @return MD5 value.
     * @throws Exception delegate from MD5 algorithm.
     */
    public String md5(String input) throws Exception {
        byte[] bytesOfMessage = input.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] theDigest = md.digest(bytesOfMessage);
        return this.bytesToHex(theDigest);
    }

    /**
     * Byte Array to HEX string.
     *
     * @param bytes input bytes.
     * @return HEX value of input bytes.
     */
    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int value = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[value >>> 4];
            hexChars[j * 2 + 1] = hexArray[value & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Distinct by multiples properties
     *
     * @param keyExtractors
     * @param <T>
     * @return
     */
    public <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

    /**
     * Distinct by a single property https://www.baeldung.com/java-streams-distinct-by
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
