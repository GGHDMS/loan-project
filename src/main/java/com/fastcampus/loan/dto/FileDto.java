package com.fastcampus.loan.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto implements Serializable {

    private String name;

    private String url;

}
