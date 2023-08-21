package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplicationDto.*;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, Request request);

    void delete(Long applicationId);

    Boolean acceptTerms(Long applicationId, AcceptTerms request);
}
