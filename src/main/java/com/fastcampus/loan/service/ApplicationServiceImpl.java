package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.AcceptTerms;
import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.ApplicationDto;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.AcceptTermsRepository;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.fastcampus.loan.dto.ApplicationDto.Request;
import static com.fastcampus.loan.dto.ApplicationDto.Response;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;
    private final TermsRepository termsRepository;
    private final AcceptTermsRepository acceptTermsRepository;

    @Override
    public Response create(Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        return modelMapper.map(application, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, Response.class);
    }

    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        application.setIsDeleted(true);

        applicationRepository.save(application);
    }

    @Override
    public Boolean acceptTerms(Long applicationId, ApplicationDto.AcceptTerms request) {
        applicationRepository.findById(applicationId).orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR)); //대출 신청정보가 존재해아한다.

        List<Terms> termList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));

        if (termList.isEmpty()) { //약관이 하나라도 있어야한다.
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if (termList.size() != acceptTermsIds.size()) { //개시한 약관 수와 신청시 동의한 약관 수 동일
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termList.stream().map(Terms::getTermsId).sorted().collect(Collectors.toList());

        if (!new HashSet<>(termsIds).containsAll(acceptTermsIds)) { //우리가 개시한 것과 다른것이 들어오면 안된다.
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for (Long termsId : termsIds) {
            acceptTermsRepository.save(AcceptTerms.builder()
                    .termsId(termsId)
                    .applicationId(applicationId)
                    .build());
        }

        return true;
    }
}
