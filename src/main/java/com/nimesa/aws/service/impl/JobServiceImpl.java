package com.nimesa.aws.service.impl;

import com.nimesa.aws.repository.dao.JobDao;
import com.nimesa.aws.service.AwsService;
import com.nimesa.aws.service.JobService;
import com.nimesa.aws.util.TransformationUtil;
import com.nimesa.aws.entity.Job;
import com.nimesa.aws.enums.Status;
import com.nimesa.aws.response.Response;
import com.nimesa.aws.response.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {


    AwsService awsService;
    JobDao jobDao;
    TransformationUtil transformationUtil;

    @Autowired
    JobServiceImpl(AwsService awsService, JobDao jobDao, TransformationUtil transformationUtil){
        this.awsService = awsService;
        this.jobDao = jobDao;
        this.transformationUtil = transformationUtil;
    }

    @Value("${aws.region}")
    private String awsRegion;

    public Response getS3BucketObjects(String bucketName){
        try{
            if(bucketName == null ){
                return transformationUtil.createResponse(null, "FAILURE", "802", "BucketName not provided");
            }
            Job job = createJobForService("FETCH_ALL_FILES_AND_PERSIST_FOR_BUCKET");
            awsService.findAllFilesForBucketAndUpdateOnDB(job, bucketName);
            return transformationUtil.createResponse(Collections.singletonList(job), "SUCCESS", "", "Job created Successfully");

        }catch (Exception e){
            return transformationUtil.createResponse(null, "FAILURE", "801", "getS3 Bucket Objects Method Failed: "+ e.getMessage());
        }

    }


    public Response getJobResult(Integer jobId){
        try{
            Job job = jobDao.getJobFromId(jobId);
            return transformationUtil.createResponse(Collections.singletonList(job), "SUCCESS", "", "Job created Successfully");
        }catch (Exception e){
            return transformationUtil.createResponse(null, "FAILURE", "601", "Error while retriving Job status: "+e.getMessage());
        }

    }

    public Response discoverServices(List<String> services) throws InterruptedException {
        try{
            List<Job> jobs = new ArrayList<>();
            if(services.contains("EC2")){
                Job job = new Job();
                job.setStatus(Status.IN_PROGRESS);
                job.setCreatedOn(LocalDateTime.now());
                job.setModifiedOn(LocalDateTime.now());
                job.setService("FETCH_EC2_DATA_IN_REGION");
                job.setRegion(awsRegion);
                try{
                    job = jobDao.createJob(job);
                    jobs.add(job);
                    awsService.discoverEC2(job);
                }catch (Exception e){
                    return transformationUtil.createResponse( null, "FAILURE", "1002", "EC2 Job Creation Failed: "+ e.getMessage());
                }

            } else if(services.contains("S3")){
                Job job = new Job();
                job.setStatus(Status.IN_PROGRESS);
                job.setCreatedOn(LocalDateTime.now());
                job.setModifiedOn(LocalDateTime.now());
                job.setService("FETCH_S3_DATA_IN_REGION");
                job.setRegion(awsRegion);
                try{
                    job = jobDao.createJob(job);
                    jobs.add(job);
                    awsService.discoverS3(job);
                }catch (Exception e){
                    return transformationUtil.createResponse( null, "FAILURE", "1002", "S3 Job Creation Failed: "+ e.getMessage());
                }

            } else{
                return transformationUtil.createResponse( null, "FAILURE", "1001", "Unknown Service provided");
            }

            return transformationUtil.createResponse(jobs, "SUCCESS", "", "Job created Successfully");
        }catch (Exception e){
            return transformationUtil.createResponse( null, "FAILURE", "1004", "DiscoverServices method failed: "+e.getMessage());
        }

    }
    public Response getDiscoveryResult(String serviceName){
        try{
            if(serviceName == null || serviceName.isBlank()){
                return transformationUtil.createResponse(null ,"FAILURE", "1101", "Service name not provided");
            }
            if(!Arrays.asList("EC2", "S3").contains(serviceName)){
                return transformationUtil.createResponse(null ,"FAILURE", "1108", "Unknown Service name provided");
            }
            List<String> serviceData = awsService.getServiceIdentifiers(serviceName);
            return transformationUtil.createResponse(serviceData, "SUCCESS", "", "Discovery Results Fetched Successfully");
        }catch (Exception e){
            return transformationUtil.createResponse(null, "FAILURE", "998", "Discovery Results Not Fetched Successfully: "+ e.getMessage());
        }

    }

    private Job createJobForService(String jobName){
        Job job = new Job();
        job.setStatus(Status.IN_PROGRESS);
        job.setCreatedOn(LocalDateTime.now());
        job.setModifiedOn(LocalDateTime.now());
        job.setService(jobName);
        job.setRegion(awsRegion);
        job = jobDao.createJob(job);

        return job;
    }

}
