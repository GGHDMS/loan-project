package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.BalanceDto.Request;
import static com.fastcampus.loan.dto.BalanceDto.Response;

public interface BalanceService {
    Response create(Long applicationId, Request request);
}
