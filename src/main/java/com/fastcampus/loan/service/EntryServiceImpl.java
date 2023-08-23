package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Entry;
import com.fastcampus.loan.dto.BalanceDto;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.fastcampus.loan.dto.EntryDto.Request;
import static com.fastcampus.loan.dto.EntryDto.Response;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService{

    private final BalanceService balanceService;

    private final EntryRepository entryRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        // 계약 체결 여부 검증
        if (isApplicationNotContracted(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        entryRepository.save(entry);

        // 대출 잔고 관리
        balanceService.create(applicationId, BalanceDto.Request.builder()
                .entryAmount(request.getEntryAmount())
                .build());

        return modelMapper.map(entry, Response.class);
    }

    private Boolean isApplicationNotContracted(Long applicationId) {
        Optional<Application> existed = applicationRepository.findById(applicationId);
        return existed.map(application -> application.getContractedAt() == null).orElse(true);
    }
}