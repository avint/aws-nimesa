package com.nimesa.aws.repository.repositoryInterface;

import com.nimesa.aws.entity.Ec2InstanceData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Ec2InstanceDataRepository extends JpaRepository<Ec2InstanceData, String> {
}
