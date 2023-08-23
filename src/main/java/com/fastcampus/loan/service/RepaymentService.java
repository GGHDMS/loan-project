package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.RepaymentDto.Request;
import static com.fastcampus.loan.dto.RepaymentDto.Response;

public interface RepaymentService {

    Response create(Long applicationId, Request request);
}
