package com.nimesa.aws.controller;

import com.nimesa.aws.response.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface JobController {
     Response discoverServices(@RequestBody List<String> services) throws InterruptedException;
     Response getJobResult(@PathVariable Integer jobId);
     Response getDiscoveryResult(@PathVariable String serviceName);
     Response getS3BucketObjects(@PathVariable String bucketName);
     Response getS3BucketObjectCount(@PathVariable String bucketName);
     Response getS3BucketObjectlike(@PathVariable String bucketName, @RequestParam String pattern);

}
