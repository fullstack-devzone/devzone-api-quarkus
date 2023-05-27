package com.sivalabs.devzone.posts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PostsDTO {
    private List<PostDTO> data;
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

    public PostsDTO(List<PostDTO> data, long totalElements, int totalPages, int pageNumber) {
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
