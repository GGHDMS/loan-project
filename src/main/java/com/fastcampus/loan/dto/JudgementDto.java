package com.fastcampus.loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JudgementDto {


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long applicationId;
        private String name;
        private BigDecimal approvalAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long judgementId;
        private Long applicationId;
        private String name;
        private BigDecimal approvalAmount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }
}


