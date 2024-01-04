package com.nimesa.aws.service.impl;

import com.nimesa.aws.entity.S3BucketData;
import com.nimesa.aws.repository.dao.S3BucketDao;
import com.nimesa.aws.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3ServiceImpl implements S3Service {

    private S3Client s3Client;
    private S3BucketDao s3BucketDao;

    @Autowired
    S3ServiceImpl(S3Client s3Client, S3BucketDao s3BucketDao){
        this.s3Client = s3Client;
        this.s3BucketDao = s3BucketDao;
    }

    public List<String> findFilesWhichMatchPattern(S3BucketData bucketData, String pattern){
        if(bucketData != null){
            return findStringsContainingSubstring(bucketData.getObjectNames(), pattern);
        }
        return null;
    }
    public S3BucketData createS3BucketData(S3BucketData s3BucketData){
        return s3BucketDao.createS3BucketData(s3BucketData);
    }

    public void findAllFilesAndUpdateOnDB(String bucketName){
        try{
            List<String> fileNames = getAllFileNames(bucketName);
            S3BucketData bucketData = findS3BucketDataByName(bucketName);
            if(fileNames != null){
                bucketData.setObjectNames(fileNames);
                bucketData.setObjCount((long) fileNames.size());
            }

            s3BucketDao.updateBucket(bucketData);
        }catch (Exception e){
            // LOG EXCEPTION
        }

    }


    public List<String> getAllFileNames(String bucketName) {
        ListObjectsResponse response = s3Client.listObjects(builder -> builder.bucket(bucketName));
        return response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    public S3BucketData findS3BucketDataByName(String bucketName){
        return s3BucketDao.findS3BucketDataByName(bucketName);
    }

    public List<String> getAllS3BucketNamesFromDB(){
        List<S3BucketData> data = s3BucketDao.findAllS3BucketData();
        return data.stream().map(S3BucketData::getName).collect(Collectors.toList());
    }

    public List<String> getAllBucketNames() {
        ListBucketsResponse response = s3Client.listBuckets();
        return response.buckets().stream()
                .map(Bucket::name)
                .collect(Collectors.toList());
    }

    private List<String> findStringsContainingSubstring(List<String> stringList, String substring) {
        return stringList.stream()
                .filter(str -> str.contains(substring))
                .collect(Collectors.toList());
    }
}
