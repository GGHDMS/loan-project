package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Judgement;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.JudgementRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.fastcampus.loan.dto.ApplicationDto.GrantAmount;
import static com.fastcampus.loan.dto.JudgementDto.Request;
import static com.fastcampus.loan.dto.JudgementDto.Response;

@Service
@RequiredArgsConstructor
public class JudgementServiceImpl implements JudgementService{

    private final JudgementRepository judgementRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        // 신청 정보 검증
        if (isApplicationNotPresent(request.getApplicationId())) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // request dto -> entity -> save
        Judgement judgement = modelMapper.map(request, Judgement.class);

        Judgement saveJudgement = judgementRepository.save(judgement);
        // save -> response dto

        return modelMapper.map(saveJudgement, Response.class);
    }

    @Override
    public Response get(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        return modelMapper.map(judgement, Response.class);
    }

    @Override
    public Response getJudgementOfApplication(Long applicationId) {
        if (isApplicationNotPresent(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgement judgement = judgementRepository.findByApplicationId(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        return modelMapper.map(judgement, Response.class);
    }

    private boolean isApplicationNotPresent(Long applicationId) {
        return applicationRepository.findById(applicationId).isEmpty();
    }

    @Override
    public Response update(Long judgementId, Request request) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        judgement.setName(request.getName());
        judgement.setApprovalAmount(request.getApprovalAmount());

        judgementRepository.save(judgement);
        return modelMapper.map(judgement, Response.class);
    }

    @Override
    public void delete(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        judgement.setIsDeleted(true);

        judgementRepository.save(judgement);
    }

    @Override
    public GrantAmount grant(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        Long applicationId = judgement.getApplicationId();

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        application.setApprovalAmount(judgement.getApprovalAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, GrantAmount.class);
    }
}
