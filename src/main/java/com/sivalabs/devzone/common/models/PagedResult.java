package com.sivalabs.devzone.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        @JsonProperty("isFirst") boolean isFirst,
        @JsonProperty("isLast") boolean isLast,
        @JsonProperty("hasNext") boolean hasNext,
        @JsonProperty("hasPrevious") boolean hasPrevious) {

    public PagedResult(List<T> data, long totalElements, int pageNumber, int totalPages) {

        this(
                data,
                totalElements,
                pageNumber,
                totalPages,
                pageNumber == 1,
                pageNumber == totalPages,
                pageNumber < totalPages,
                pageNumber > 1);
    }

    public PagedResult(
            List<T> data,
            long totalElements,
            int pageNumber,
            int totalPages,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious) {
        this.data = data;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
}
