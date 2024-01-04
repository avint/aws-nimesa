package com.nimesa.aws.controller.Impl;

import com.nimesa.aws.controller.JobController;
import com.nimesa.aws.response.Response;
import com.nimesa.aws.service.AwsService;
import com.nimesa.aws.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
public class JobControllerImpl implements JobController {

    JobService jobService;
    AwsService awsService;

    @Autowired
    JobControllerImpl(JobService jobService, AwsService awsService){
        this.jobService = jobService;
        this.awsService = awsService;
    }

    @GetMapping("/job/discoverServices")
    public Response discoverServices(@RequestBody List<String> services) throws InterruptedException {
        return jobService.discoverServices(services);
    }
    @GetMapping("/job/getJobResult/{jobId}")
    public Response getJobResult(@PathVariable Integer jobId){
        return jobService.getJobResult(jobId);
    }

    @GetMapping("/job/getDiscoveryResult/{serviceName}")
    public Response getDiscoveryResult(@PathVariable String serviceName){
        return jobService.getDiscoveryResult(serviceName);
    }

    @GetMapping("/job/getS3BucketObjects/{bucketName}")
    public Response getS3BucketObjects(@PathVariable String bucketName){
        return jobService.getS3BucketObjects(bucketName);
    }

    @GetMapping("/job/getS3BucketObjectCount/{bucketName}")
    public Response getS3BucketObjectCount(@PathVariable String bucketName){
        return awsService.getS3BucketObjectCount(bucketName);
    }

    @GetMapping("/job/getS3BucketObjectlike/{bucketName}")
    public Response getS3BucketObjectlike(@PathVariable String bucketName, @RequestParam String pattern){
        return awsService.getS3BucketObjectlike(bucketName, pattern);
    }
}
