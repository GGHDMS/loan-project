package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.CounselDto.Request;
import com.fastcampus.loan.dto.CounselDto.Response;

public interface CounselService {

    Response create(Request request);

    Response get(Long counselId);

    Response update(Long counselId, Request request);
}
