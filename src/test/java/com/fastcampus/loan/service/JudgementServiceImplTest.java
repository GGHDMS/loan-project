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

import java.util.Optional;

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
}
