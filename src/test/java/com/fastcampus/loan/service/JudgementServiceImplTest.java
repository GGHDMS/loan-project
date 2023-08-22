package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Judgement;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.JudgementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static com.fastcampus.loan.dto.ApplicationDto.GrantAmount;
import static com.fastcampus.loan.dto.JudgementDto.Request;
import static com.fastcampus.loan.dto.JudgementDto.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JudgementServiceImplTest {
    @InjectMocks
    JudgementServiceImpl judgementService;

    @Mock
    private JudgementRepository judgementRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void 새로운_심사가들어오면_응답을줘야한다() {
        Long applicationId = 1L;
        Long judgementId = 1L;

        Judgement judgement = Judgement.builder()
                .applicationId(applicationId)
                .judgementId(judgementId)
                .build();

        Request request = Request.builder()
                .applicationId(applicationId)
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(judgementRepository.save(any(Judgement.class))).thenReturn(judgement);

        Response response = judgementService.create(request);

        assertThat(response.getApplicationId()).isEqualTo(applicationId);
        assertThat(response.getJudgementId()).isEqualTo(judgementId);
    }

    @Test
    void 심사아이디가_존재하면_심사에관한_응답을준다() {
        Long judgementId = 1L;

        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .build();

        when(judgementRepository.findById(judgementId)).thenReturn(Optional.ofNullable(judgement));

        Response response = judgementService.get(judgementId);
        assertThat(response.getJudgementId()).isEqualTo(judgementId);
    }

    @Test
    void 신청아이디가_존재하면_심사에관한_응답을준다() {
        Long applicationId = 1L;
        Long judgementId = 1L;

        Judgement judgement = Judgement.builder()
                .applicationId(applicationId)
                .judgementId(judgementId)
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(judgementRepository.findByApplicationId(applicationId)).thenReturn(Optional.ofNullable(judgement));

        Response response = judgementService.getJudgementOfApplication(judgementId);

        assertThat(response.getApplicationId()).isEqualTo(applicationId);
        assertThat(response.getJudgementId()).isEqualTo(judgementId);
    }


    @Test
    void 존재하는심사아이디로_수정요청이오면_수정후_응답을준다() {
        Long judgementId = 1L;
        Request request = Request.builder()
                .name("name")
                .approvalAmount(BigDecimal.valueOf(50000))
                .build();

        when(judgementRepository.findById(judgementId)).thenReturn(Optional.ofNullable(Judgement.builder().build()));
        when(judgementRepository.save(any(Judgement.class))).thenReturn(null);

        Response response = judgementService.update(judgementId, request);

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getApprovalAmount()).isEqualTo(request.getApprovalAmount());
    }

    @Test
    void 존재하는아이디로_삭제요청이들어오면_삭제한다() {
        Long judgementId = 1L;
        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .build();

        when(judgementRepository.findById(judgementId)).thenReturn(Optional.ofNullable(judgement));
        when(judgementRepository.save(any(Judgement.class))).thenReturn(null);

        judgementService.delete(judgementId);

        assertThat(judgement.getIsDeleted()).isTrue();
    }

    @Test
    void 심사금액부여_요청이들어올때_아이디값들이_존재하면_업데이트한다() {
        Long judgementId = 1L;
        Long applicationId = 1L;

        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .applicationId(applicationId)
                .approvalAmount(BigDecimal.valueOf(50000))
                .build();

        Application application = Application.builder()
                .applicationId(applicationId)
                .build();

        when(judgementRepository.findById(judgementId)).thenReturn(Optional.ofNullable(judgement));
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(null);

        GrantAmount grantAmount = judgementService.grant(judgementId);
        assertThat(grantAmount.getApplicationId()).isEqualTo(judgement.getApplicationId());
        assertThat(grantAmount.getApprovalAmount()).isEqualTo(judgement.getApprovalAmount());


    }
}
