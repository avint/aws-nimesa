package com.nimesa.aws.repository.repositoryInterface;

import com.nimesa.aws.entity.S3BucketData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3BucketDataRepository extends JpaRepository<S3BucketData, String> {

}
