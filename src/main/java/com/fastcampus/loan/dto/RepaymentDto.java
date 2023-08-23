package com.fastcampus.loan.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepaymentDto implements Serializable {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private BigDecimal repaymentAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long applicationId;
        private BigDecimal repaymentAmount;
        private BigDecimal balance;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
