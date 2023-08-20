package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.fastcampus.loan.dto.TermsDto.Request;
import static com.fastcampus.loan.dto.TermsDto.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TermsServiceTest {

    @InjectMocks
    TermsServiceImpl termsService;

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;


    @Test
    void 새로운약관정보를_요청했을때_응답를준다() {
        Request request = Request.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://qweradsf.com")
                .build();

        Terms entity = Terms.builder()
                .name("대출 이용 약관")
                .termsDetailUrl("https://qweradsf.com")
                .build();


        when(termsRepository.save(any(Terms.class))).thenReturn(entity);

        Response response = termsService.create(request);

        assertThat(response.getName()).isEqualTo(entity.getName());
        assertThat(response.getTermsDetailUrl()).isEqualTo(entity.getTermsDetailUrl());
    }

    @Test
    void 요청이오면_모든약관을_리턴한다() {
        Terms entity1 = Terms.builder()
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://adfadf.com")
                .build();

        Terms entity2 = Terms.builder()
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://adfadf.com")
                .build();

        when(termsRepository.findAll()).thenReturn(List.of(entity1, entity2));


        List<Response> responseList = termsService.getAll();

        assertThat(responseList.size()).isEqualTo(2);
    }
}
