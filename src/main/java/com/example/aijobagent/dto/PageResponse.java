package com.example.aijobagent.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
}
