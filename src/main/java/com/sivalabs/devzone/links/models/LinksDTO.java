package com.sivalabs.devzone.links.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.devzone.links.entities.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class LinksDTO {
    private List<LinkDTO> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;

    @JsonProperty("isFirst")
    private boolean isFirst;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    public LinksDTO(List<LinkDTO> data, long totalElements, int totalPages, int pageNumber) {
        this.setData(data);
        this.setTotalElements(totalElements);
        this.setPageNumber(pageNumber);
        this.setTotalPages(totalPages);
        this.setFirst(pageNumber == 1);
        this.setLast(pageNumber == totalPages);
        this.setHasNext(pageNumber < totalPages);
        this.setHasPrevious(pageNumber > 1);
    }
}
