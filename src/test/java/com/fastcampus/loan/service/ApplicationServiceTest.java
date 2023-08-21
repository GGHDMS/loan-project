package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.AcceptTerms;
import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.ApplicationDto;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.repository.AcceptTermsRepository;
import com.fastcampus.loan.repository.ApplicationRepository;
import com.fastcampus.loan.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.fastcampus.loan.dto.ApplicationDto.Request;
import static com.fastcampus.loan.dto.ApplicationDto.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private AcceptTermsRepository acceptTermsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void 새로운_대출신청_요청이왔을때_응답을_준다() {
        Application entity = Application.builder()
                .name("Member Heo")
                .cellPhone("010-1111-1111")
                .email("mail@mail.com")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        Request request = Request.builder()
                .name("Member Heo")
                .cellPhone("010-1111-1111")
                .email("mail@mail.com")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        when(applicationRepository.save(any(Application.class))).thenReturn(entity);

        Response response = applicationService.create(request);

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getHopeAmount()).isEqualTo(request.getHopeAmount());
    }

    @Test
    void 존재하는_어플리케이션아이디를_조회하면_엔티티를_응답으로_준다() {
        Long applicationId = 1L;

        Application entity = Application.builder()
                .applicationId(applicationId)
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(entity));

        Response response = applicationService.get(applicationId);

        assertThat(response.getApplicationId()).isEqualTo(applicationId);

    }

    @Test
    void 요청DTO와_함께_존재하는아이디로_요청이오면_업데이트해야한다() {
        Long applicationId = 1L;

        Application entity = Application.builder()
                .applicationId(applicationId)
                .hopeAmount(BigDecimal.valueOf(1000000))
                .applicationId(applicationId)
                .build();

        Request request = Request.builder()
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(entity));
        when(applicationRepository.save(any(Application.class))).thenReturn(entity);

        Response response = applicationService.update(applicationId, request);

        assertThat(response.getApplicationId()).isEqualTo(applicationId);
        assertThat(response.getHopeAmount()).isEqualTo(request.getHopeAmount());
    }

    @Test
    void 존재하는아이디로_삭제요청이오면_삭제한다() {
        Long applicationId = 1L;

        Application entity = Application.builder()
                .applicationId(applicationId)
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(entity));
        when(applicationRepository.save(any(Application.class))).thenReturn(entity);

        applicationService.delete(applicationId);

        assertThat(entity.getIsDeleted()).isTrue();
    }

    @Test
    void 신청정보의_약관을입력받으면_약관을_등록해야한다() {
        Terms entity1 = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://adfadf.com")
                .build();

        Terms entity2 = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://adfadf.com")
                .build();

        ApplicationDto.AcceptTerms request = ApplicationDto.AcceptTerms.builder()
                .acceptTermsIds(List.of(1L, 2L))
                .build();

        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(List.of(entity1, entity2));
        when(acceptTermsRepository.save(any(AcceptTerms.class))).thenReturn(AcceptTerms.builder().build());

        Boolean result = applicationService.acceptTerms(applicationId, request);

        assertThat(result).isTrue();
    }

    @Test
    void 모든약관을_동의하지않으면_에러를_반환한다() {
        Terms entity1 = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://adfadf.com")
                .build();

        Terms entity2 = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://adfadf.com")
                .build();

        ApplicationDto.AcceptTerms request = ApplicationDto.AcceptTerms.builder()
                .acceptTermsIds(List.of(1L))
                .build();

        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(List.of(entity1, entity2));

        assertThatThrownBy(() -> applicationService.acceptTerms(applicationId, request))
                .isInstanceOf(BaseException.class);
    }

    @Test
    void 존재하지않은_약관을_입력하면_에러를_반환한다() {
        Terms entity1 = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://adfadf.com")
                .build();

        Terms entity2 = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://adfadf.com")
                .build();

        ApplicationDto.AcceptTerms request = ApplicationDto.AcceptTerms.builder()
                .acceptTermsIds(List.of(1L, 3L))
                .build();

        Long applicationId = 1L;

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(List.of(entity1, entity2));

        assertThatThrownBy(() -> applicationService.acceptTerms(applicationId, request))
                .isInstanceOf(BaseException.class);
    }
}
