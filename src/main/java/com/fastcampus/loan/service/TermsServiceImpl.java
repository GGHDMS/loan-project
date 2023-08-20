package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.fastcampus.loan.dto.TermsDto.Request;
import static com.fastcampus.loan.dto.TermsDto.Response;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService{

    private final TermsRepository termsRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {

        Terms terms = modelMapper.map(request, Terms.class);

        Terms entity = termsRepository.save(terms);

        return modelMapper.map(entity, Response.class);
    }

    @Override
    public List<Response> getAll() {
        List<Terms> termList = termsRepository.findAll();

        return termList.stream().map(m -> modelMapper.map(m, Response.class)).collect(Collectors.toList());
    }

}
