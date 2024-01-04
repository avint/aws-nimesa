package com.nimesa.aws.service;

import com.nimesa.aws.entity.Job;
import com.nimesa.aws.entity.S3BucketData;
import com.nimesa.aws.response.Response;

import java.util.List;

public interface AwsService {
    public Response getS3BucketObjectlike(String bucketName, String pattern);
    public Response getS3BucketObjectCount(String bucketName);
    public S3BucketData findS3BucketDataByName(String bucketName);
    public void findAllFilesForBucketAndUpdateOnDB(Job job, String bucketName);
    public List<String> getServiceIdentifiers(String serviceName);
    public void discoverEC2(Job job);
    public void discoverS3(Job job) throws InterruptedException;
}
