package com.fastcampus.loan.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

public class BalanceDto implements Serializable {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long applicationId;
        private BigDecimal entryAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private Long applicationId;
        private BigDecimal beforeEntryAmount;
        private BigDecimal afterEntryAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RepaymentRequest {

        public enum RepaymentType {
            ADD,
            REMOVE
        }

        private RepaymentType type;
        private BigDecimal repaymentAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long balanceId;
        private Long applicationId;
        private BigDecimal balance;
    }

}
