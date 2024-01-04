package com.nimesa.aws.service;

import com.nimesa.aws.response.Response;

import java.util.List;

public interface JobService {
    Response getS3BucketObjects(String bucketName);
    Response getJobResult(Integer jobId);
    Response discoverServices(List<String> services) throws InterruptedException;
    Response getDiscoveryResult(String serviceName);

}
