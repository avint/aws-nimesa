package com.nimesa.aws.repository.dao.Impl;

import com.nimesa.aws.entity.Job;

import com.nimesa.aws.repository.dao.JobDao;
import com.nimesa.aws.repository.repositoryInterface.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JobDaoImpl implements JobDao {

    JobRepository jobRepository;

    @Autowired
    JobDaoImpl(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }
    @Transactional
    public Job createJob(Job job){
        return jobRepository.save(job);
    }
    @Transactional
    public Job updateJob(Job job){
        if(job.getId() == null){
            return null;
        }
        if(jobRepository.existsById(job.getId())){
            return jobRepository.save(job);
        }else {
            return null;
        }
    }

    public Job getJobFromId(Integer jobId){
        return jobRepository.findById(jobId).get();
    }

}
