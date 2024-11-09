package com.medic.system.helpers;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ThymeleafHelper {

    public String generateSortUrl(Pageable pageable, String route, String fieldName, String extraParams) {
        return generateSortUrl(pageable, route, List.of(fieldName), extraParams);
    }

    public String generateSortUrl(Pageable pageable, String route, List<String> fieldNames, String extraParams) {
        // Determine the sorting direction for the first field
        boolean isAscending = pageable.getSort()
                .getOrderFor(fieldNames.get(0)) == null ||
                pageable.getSort().getOrderFor(fieldNames.get(0)).isAscending();
        String sortOrder = isAscending ? "DESC" : "ASC";

        String sortParams = fieldNames.stream()
                .map(field -> field + "," + sortOrder)
                .collect(Collectors.joining("&sort="));

        // Construct the full URL
        return String.format("%s?page=%d&size=%d&sort=%s&%s",
                route,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortParams,
                (extraParams != null ? extraParams : ""));
    }

    public String toQueryString(Object form) {
        return Arrays.stream(form.getClass().getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .map(f -> {
                    try {
                        Object value = f.get(form);
                        return value != null ? f.getName() + "=" + value : null;
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                })
                .filter(s -> s != null)
                .collect(Collectors.joining("&"));
    }
}