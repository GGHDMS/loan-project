package com.fastcampus.loan.service;

import java.util.List;

import static com.fastcampus.loan.dto.RepaymentDto.*;

public interface RepaymentService {

    Response create(Long applicationId, Request request);

    List<ListResponse> getList(Long applicationId);

    UpdateResponse update(Long repaymentId, Request request);
}
