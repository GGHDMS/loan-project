package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplicationDto.Request;
import static com.fastcampus.loan.dto.ApplicationDto.Response;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);
}
