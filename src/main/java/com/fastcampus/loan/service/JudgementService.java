package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.JudgementDto.Request;
import static com.fastcampus.loan.dto.JudgementDto.Response;

public interface JudgementService {

    Response create(Request request);
}
