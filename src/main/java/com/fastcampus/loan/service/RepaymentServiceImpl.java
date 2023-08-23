package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.BalanceDto;
import com.fastcampus.loan.dto.BalanceDto.RepaymentRequest;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import com.fastcampus.loan.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fastcampus.loan.dto.RepaymentDto.*;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService{

    private final BalanceService balanceService;

    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {

        // validation
        // 1. 계약을 완료한 신청 정보
        // 2. 집행이 되어있어야 함
        if (isApplicationNotRepayable(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        // 잔고
        // balance : 500 - 100 = 400
        BalanceDto.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                RepaymentRequest.builder()
                        .type(RepaymentRequest.RepaymentType.REMOVE)
                        .repaymentAmount(request.getRepaymentAmount())
                        .build());

        Response response = modelMapper.map(repayment, Response.class);

        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    @Override
    public List<ListResponse> getList(Long applicationId) {
        List<Repayment> repaymentList = repaymentRepository.findAllByApplicationId(applicationId);

        return repaymentList.stream().map(r -> modelMapper.map(r, ListResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UpdateResponse update(Long repaymentId, Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        Long applicationId = repayment.getApplicationId();

        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
                RepaymentRequest.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(RepaymentRequest.RepaymentType.ADD)
                        .build()
        );

        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        BalanceDto.Response updatedBalace = balanceService.repaymentUpdate(applicationId,
                RepaymentRequest.builder()
                        .repaymentAmount(repayment.getRepaymentAmount())
                        .type(RepaymentRequest.RepaymentType.REMOVE)
                        .build()
        );


        return UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(request.getRepaymentAmount())
                .balance(updatedBalace.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(repayment.getUpdatedAt())
                .build();
    }

    private boolean isApplicationNotRepayable(Long applicationId) {
        Optional<Application> existedApplication = applicationRepository.findById(applicationId);

        if (existedApplication.isEmpty()) {
            return true;
        }

        if (existedApplication.get().getContractedAt() == null) {
            return true;
        }

        Optional<Entry> existedEntry = entryRepository.findByApplicationId(applicationId);
        return existedEntry.isEmpty();
    }
}
