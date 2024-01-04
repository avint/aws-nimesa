package com.nimesa.aws.repository.dao;

import com.nimesa.aws.entity.Job;

public interface JobDao {
    public Job createJob(Job job);
    public Job updateJob(Job job);
    public Job getJobFromId(Integer jobId);
}
