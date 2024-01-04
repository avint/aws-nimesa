package com.nimesa.aws.repository.repositoryInterface;

import com.nimesa.aws.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {

}
