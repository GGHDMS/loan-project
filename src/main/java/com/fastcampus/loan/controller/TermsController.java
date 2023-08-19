package com.fastcampus.loan.controller;


import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fastcampus.loan.dto.TermsDto.Request;
import static com.fastcampus.loan.dto.TermsDto.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController extends AbstractController{

    private final TermsService termsService;

    @PostMapping
    ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(termsService.create(request));
    }
}
