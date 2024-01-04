package com.nimesa.aws.repository.dao.Impl;

import com.nimesa.aws.entity.S3BucketData;
import com.nimesa.aws.repository.dao.S3BucketDao;
import com.nimesa.aws.repository.repositoryInterface.S3BucketDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class S3BucketDaoImpl implements S3BucketDao {

    S3BucketDataRepository s3BucketDataRepository;

    @Autowired
    S3BucketDaoImpl(S3BucketDataRepository s3BucketDataRepository){
        this.s3BucketDataRepository = s3BucketDataRepository;
    }

    public S3BucketData findS3BucketDataByName(String bucketName){
        Optional<S3BucketData> s3BucketData = s3BucketDataRepository.findById(bucketName);
        if(s3BucketData.isPresent()){
            return s3BucketData.get();
        }
        return null;
    }


    public S3BucketData updateBucket(S3BucketData s3BucketData){
        if(s3BucketData.getName() == null){
            return null;
        }
        return s3BucketDataRepository.save(s3BucketData);
    }
    public List<S3BucketData> findAllS3BucketData(){
        return s3BucketDataRepository.findAll();
    }
    public S3BucketData createS3BucketData(S3BucketData data){
        Optional<S3BucketData> existingEntry = s3BucketDataRepository.findById(data.getName());
        if( !existingEntry.isPresent()){
            return s3BucketDataRepository.save(data);
        }else{
            return existingEntry.get();
        }

    }
}
