package org.example.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Data
public class PageDTO<T> {
    private int page;
    private int totalPages;
    private List<T> items;

    public <P> PageDTO(Page<P> page, Function<P, T> converter) {
        this.page = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.items = page.map(converter).getContent();
    }

}
