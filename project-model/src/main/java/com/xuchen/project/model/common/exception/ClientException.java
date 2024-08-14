package com.xuchen.project.model.common.exception;

import com.xuchen.project.model.common.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientException extends RuntimeException{
    private final ResponseStatus status;
}
