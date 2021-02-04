package com.viktoria.sharent;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.viktoria.sharent.exception.SharentalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by manishtamaria on 14/2/16.
 */
public class SharentalUtils {
    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
    private static Logger logger = LoggerFactory.getLogger(SharentalUtils.class);

    public static LocalDateTime currentTime() {
        return LocalDateTime.now();
    }


    /**
     * @return Date with time of 00:00:00
     */
    public static Date getStartingTimeOfDate (Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * @return Date with time of 23:59:59
     */

    public static Date getEndTimeOfDate (Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }

    public static <T> T getObjectFromJson(String json, Class<T> clazz) throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructType(clazz);
        ObjectReader reader = objectMapper.reader(type);
        return reader.readValue(json);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T getMapFromJson(String json, Class<?> keyClass, Class<?> valueClass,
                                       Class<? extends Map> mapClass) throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
        ObjectReader reader = objectMapper.reader(type);
        return reader.readValue(json);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T getCollectionFromJson(String json, Class<?> clazz, Class<? extends Collection> collectionClazz)
            throws JsonProcessingException, IOException {
        ObjectMapper objectMapper =new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(collectionClazz, clazz);
        ObjectReader reader = objectMapper.reader(type);
        return reader.readValue(json);
    }

    public static String getJsonFromObject(Object obj) {
        if (obj == null)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Unable to convert to json string", e);
            return null;
        }
    }

    public static void raiseUnexpectedCondition(Exception e) {
        throw new SharentalException(e);
    }

    public static void raiseUnexpectedCondition(String message) {
        throw new SharentalException(message);
    }

    public static Long getUniqueId(Integer portId, long id) {
        StringBuilder sb = new StringBuilder().append(portId);

        double pow = Math.pow(10, 3 - sb.length());
        long power = (long) pow;

        sb.append(String.valueOf(power).substring(1)).append(id);
        return Long.parseLong(sb.toString());
    }

    public static void raiseException(String string) {
        throw new SharentalException(string);
    }

}
