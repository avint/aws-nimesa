package com.nimesa.aws.service;

import com.nimesa.aws.entity.Job;
import com.nimesa.aws.entity.S3BucketData;
import com.nimesa.aws.response.Response;

import java.util.List;

public interface S3Service {
    List<String> findFilesWhichMatchPattern(S3BucketData bucketName, String pattern);
    S3BucketData createS3BucketData(S3BucketData s3BucketData);
    void findAllFilesAndUpdateOnDB(String bucketName);
    List<String> getAllFileNames(String bucketName);
    S3BucketData findS3BucketDataByName(String bucketName);
    List<String> getAllS3BucketNamesFromDB();
    List<String> getAllBucketNames();

}
