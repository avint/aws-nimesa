package com.nimesa.aws.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimesa.aws.config.ThreadPoolConfig;
import com.nimesa.aws.repository.dao.JobDao;
import com.nimesa.aws.service.AwsService;
import com.nimesa.aws.service.Ec2Service;
import com.nimesa.aws.service.S3Service;
import com.nimesa.aws.util.TransformationUtil;
import com.nimesa.aws.entity.Ec2InstanceData;
import com.nimesa.aws.entity.Job;
import com.nimesa.aws.entity.S3BucketData;
import com.nimesa.aws.enums.Status;
import com.nimesa.aws.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.model.Instance;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class AwsServiceImpl implements AwsService {
    private Ec2Service ec2Service;
    private S3Service s3Service;
    private TransformationUtil transformationUtil;

    @Qualifier("AwsThreadPool")
    private  Executor AwsThreadPool;
    private JobDao jobDao;
    @Autowired
    AwsServiceImpl(Ec2Service ec2Service, S3Service s3Service, JobDao jobDao, TransformationUtil transformationUtil){
        this.ec2Service = ec2Service;
        this.s3Service = s3Service;
        this.jobDao = jobDao;
        this.transformationUtil = transformationUtil;
    }

    public Response getS3BucketObjectlike(String bucketName, String pattern){

        try{
            if(bucketName == null){
                return transformationUtil.createResponse(null ,"FAILURE", "1101", "Bucket Name not provided");
            }
            if(pattern == null){
                return transformationUtil.createResponse(null ,"FAILURE", "1102", "Search pattern not provided");
            }
            S3BucketData bucketData = s3Service.findS3BucketDataByName(bucketName);
            if(bucketData == null){
                return transformationUtil.createResponse(null ,"FAILURE", "901", "Bucket not found on Database");
            }
            List<String> fileNames = s3Service.findFilesWhichMatchPattern(bucketData, pattern);
            return transformationUtil.createResponse(fileNames,"SUCCESS", "", "Search Success for given Pattern");
        }catch (Exception e){
            return transformationUtil.createResponse(null ,"FAILURE", "900", "Error while search files: "+e.getMessage());
        }


    }
    public Response getS3BucketObjectCount(String bucketName){
        try{
            if(bucketName == null){
                return transformationUtil.createResponse(null ,"FAILURE", "1101", "Bucket Name not provided");
            }
            S3BucketData data = findS3BucketDataByName(bucketName);
            if(data != null){
                return transformationUtil.createResponse(Collections.singletonList(data.getObjCount()),"SUCCESS", "", "Count Fetched Successfully");
            }else{
                return transformationUtil.createResponse(null,"FAILURE", "1110", "Unable to fetch count for bucketName: "+bucketName);
            }

        }catch (Exception e){
            return transformationUtil.createResponse(null ,"FAILURE", "1101", "Error while fetching count data on bucket: "+e.getMessage());
        }

    }
    public S3BucketData findS3BucketDataByName(String bucketName){
        return s3Service.findS3BucketDataByName(bucketName);
    }

    @Async("AwsThreadPool")
    public void findAllFilesForBucketAndUpdateOnDB(Job job, String bucketName){
        try{
            job.setStatus(Status.SUCCESS);
            job.setModifiedOn(LocalDateTime.now());
            s3Service.findAllFilesAndUpdateOnDB(bucketName);

        }catch (Exception e){
            job.setStatus(Status.FAILURE);
            job.setMessage(e.getMessage());
            job.setModifiedOn(LocalDateTime.now());

        }
        jobDao.updateJob(job);
    }

    @Async("AwsThreadPool")
    public void discoverEC2(Job job){
        try{
            List<Instance> instances =  ec2Service.getEc2Instances();

            for(Instance instance: instances){
                Ec2InstanceData data = new Ec2InstanceData();
                data.setInstanceId(instance.instanceId());
                try{
                    //Converting the metaData to JSON to store it on DB
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Instance Type", instance.instanceTypeAsString());
                    map.put("Architecture", instance.architectureAsString());
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    data.setInstanceMetaData(ow.writeValueAsString(map));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                ec2Service.createEc2InstanceData(data);
            }
            job.setStatus(Status.SUCCESS);
            job.setModifiedOn(LocalDateTime.now());
        }catch (Exception e){
            job.setStatus(Status.FAILURE);
            job.setMessage(e.getMessage());
            job.setModifiedOn(LocalDateTime.now());
        }
        jobDao.updateJob(job);
    }


    public List<String> getServiceIdentifiers(String serviceName){
        if(serviceName.equals("EC2")){
            return ec2Service.getAllEc2InstanceIds();
        } else if (serviceName.equals("S3")) {
            return s3Service.getAllS3BucketNamesFromDB();
        }
        return null;
    }
    @Async
    public void discoverS3(Job job) throws InterruptedException {
        Thread.sleep(5000);
        try{
            List<String> buckets = s3Service.getAllBucketNames();

            for(String bucketName: buckets){
                S3BucketData data = new S3BucketData();
                data.setName(bucketName);
                s3Service.createS3BucketData(data);
            }
            job.setStatus(Status.SUCCESS);
            job.setModifiedOn(LocalDateTime.now());
            jobDao.updateJob(job);

        }catch (Exception e){
            job.setStatus(Status.FAILURE);
            job.setMessage(e.getMessage());
            job.setModifiedOn(LocalDateTime.now());
            jobDao.updateJob(job);
        }
    }
}
