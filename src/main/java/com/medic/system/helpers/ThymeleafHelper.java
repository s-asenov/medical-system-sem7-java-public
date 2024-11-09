package com.medic.system.helpers;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ThymeleafHelper {

    /**
     * Generates a URL for sorting with the given parameters.
     *
     * @param pageable the pageable object used for sorting and pagination
     * @param route the base url
     * @param fieldName the field name to sort by
     * @param extraParams any extra parameters to include in the URL
     * @return the generated URL
     */
    public String generateSortUrl(Pageable pageable, String route, String fieldName, String extraParams) {
        return generateSortUrl(pageable, route, List.of(fieldName), extraParams);
    }

    /**
     * Generates a URL for sorting with the given parameters.
     *
     * @param pageable the pageable object used for sorting and pagination
     * @param route the base url
     * @param fieldNames the field names to sort by
     * @param extraParams any extra parameters to include in the URL
     * @return the generated URL
     */
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

    /**
     * Generates query params given an object.
     * ex: ?name=John&age=25
     * @param form
     * @return the query string
     */
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

    /**
     * Generates a URL for pagination with the given parameters.
     *
     * @param route the base url
     * @param pageable the pageable object used for sorting and pagination
     * @param page the page number to generate the URL for
     * @param extraParams any extra parameters to include in the URL
     * @return the generated URL
     */
    public String generatePaginationUrl(String route, Pageable pageable, Long page, String extraParams)
    {
        String sortParams = pageable.getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection())
                .collect(Collectors.joining("&sort="));

        return String.format("%s?page=%d&size=%d&sort=%s&%s",
                route,
                page,
                pageable.getPageSize(),
                sortParams,
                (extraParams != null ? extraParams : ""));
    }
}