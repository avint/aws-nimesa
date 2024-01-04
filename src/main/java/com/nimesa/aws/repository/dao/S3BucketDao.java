package com.nimesa.aws.repository.dao;

import com.nimesa.aws.entity.S3BucketData;

import java.util.List;

public interface S3BucketDao {
     S3BucketData findS3BucketDataByName(String bucketName);
     S3BucketData updateBucket(S3BucketData s3BucketData);
     List<S3BucketData> findAllS3BucketData();
     S3BucketData createS3BucketData(S3BucketData data);

}
