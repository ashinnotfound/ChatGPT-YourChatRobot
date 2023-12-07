package com.ashin.entity.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class ChatResultDTO {
    String StringResult;
    InputStream inputStreamResult;
    byte[] bytesResult;
}
