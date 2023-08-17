package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Counsel;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.repository.CounselRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static com.fastcampus.loan.dto.CounselDto.Request;
import static com.fastcampus.loan.dto.CounselDto.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        assertThat(response.getName()).isEqualTo(entity.getName());
    }

    @Test
    void 대출아이디를_입력받았을때_아이디가_존재하면_응답을준다() {
        Long counselId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.findById(counselId)).thenReturn(Optional.ofNullable(entity));

        Response response = counselService.get(counselId);
        assertThat(response.getCounselId()).isEqualTo(counselId);
    }

    @Test
    void 대출아이디를_입력받았을때_아이디가_존재하지않으면_예외를던진다() {
        Long counselId = 2L;

        when(counselRepository.findById(counselId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> counselService.get(counselId))
                .isInstanceOf(BaseException.class);
    }
}
