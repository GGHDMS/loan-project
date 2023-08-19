package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.TermsDto.Request;
import static com.fastcampus.loan.dto.TermsDto.Response;

public interface TermsService {

    Response create(Request request);
}
