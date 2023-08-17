package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Counsel;
import com.fastcampus.loan.repository.CounselRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static com.fastcampus.loan.dto.CounselDto.Request;
import static com.fastcampus.loan.dto.CounselDto.Response;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CounselServiceTest {
    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;
    @Spy
    private ModelMapper modelMapper;

    @Test
    void 대출요청을_받으면_대출응답을_리턴해야한다() {
        Counsel entity = Counsel.builder()
                .name("Member Hello")
                .cellPhone("010-1111-1111")
                .email("abc@mail.c")
                .memo("10억 대출")
                .zipcode("12345")
                .address("서울특별시 사동구 사동동")
                .addressDetail("101동 101호")
                .build();

        Request request = Request.builder()
                .name("Member Hello")
                .cellPhone("010-1111-1111")
                .email("abc@mail.c")
                .memo("10억 대출")
                .zipcode("12345")
                .address("서울특별시 사동구 사동동")
                .addressDetail("101동 101호")
                .build();

        when(counselRepository.save(any(Counsel.class))).thenReturn(entity);
        Response response = counselService.create(request);
        Assertions.assertThat(response.getName()).isEqualTo(entity.getName());
    }

}
