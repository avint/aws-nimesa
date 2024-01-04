package com.nimesa.aws.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
    T data;
    ResponseStatus status;
}
