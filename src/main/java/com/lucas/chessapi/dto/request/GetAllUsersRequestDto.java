package com.lucas.chessapi.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public record GetAllUsersRequestDto(Integer pageNumber, Integer pageSize, List<String> sortFields) {
    public PageRequest generatePagination() {
        var sort = generateSort();
        return PageRequest.of(
                pageNumber,
                pageSize,
                sort
        );
    }

    private Sort generateSort() {
        var fields = new Sort.Order[sortFields.size()];
        sortFields.stream()
                .map(Sort.Order::asc)
                .toList().toArray(fields);
        return Sort.by(fields);
    }
}
