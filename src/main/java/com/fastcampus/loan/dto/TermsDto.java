package com.fastcampus.loan.dto;

import lombok.*;

import java.time.LocalDateTime;

public class TermsDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String termsDetailUrl;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long termsId;
        private String name;
        private String termsDetailUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
