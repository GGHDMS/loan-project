package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Application;
import com.fastcampus.loan.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static com.fastcampus.loan.dto.ApplicationDto.Request;
import static com.fastcampus.loan.dto.ApplicationDto.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

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

}
