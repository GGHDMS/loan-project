package com.fastcampus.loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ApplicationDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String cellPhone;
        private String email;
        private BigDecimal hopeAmount;
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String name;
        private String cellPhone;
        private String email;
        private BigDecimal hopeAmount;
        private LocalDateTime appliedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
