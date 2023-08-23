package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.EntryDto.Request;
import static com.fastcampus.loan.dto.EntryDto.Response;

public interface EntryService {

    Response create(Long application, Request request);
}
