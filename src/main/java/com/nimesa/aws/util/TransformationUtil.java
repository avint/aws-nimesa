package com.nimesa.aws.util;

import com.nimesa.aws.entity.Job;
import com.nimesa.aws.response.Response;
import com.nimesa.aws.response.ResponseStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class TransformationUtil {
    public Response<List<?>> createResponse(List<?> data, String status, String code, String message){

        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setCode(code);
        responseStatus.setTime(LocalDateTime.now());
        responseStatus.setStatus(status);
        responseStatus.setMessage(message);

        Response response = new Response<>();
        response.setStatus(responseStatus);
        response.setData(data);

        return response;
    }
}
