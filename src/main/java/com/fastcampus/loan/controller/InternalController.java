package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.fastcampus.loan.dto.EntryDto.Request;
import static com.fastcampus.loan.dto.EntryDto.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController{

    private final EntryService entryService;

    @PostMapping("/{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        return ok(entryService.create(applicationId, request));
    }
}