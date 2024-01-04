package com.nimesa.aws.repository.dao.Impl;

import com.nimesa.aws.entity.Ec2InstanceData;
import com.nimesa.aws.repository.dao.Ec2InstanceDao;
import com.nimesa.aws.repository.repositoryInterface.Ec2InstanceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class Ec2InstanceDaoImpl implements Ec2InstanceDao{

    Ec2InstanceDataRepository ec2InstanceDataRepository;

    @Autowired
    Ec2InstanceDaoImpl(Ec2InstanceDataRepository ec2InstanceDataRepository){
        this.ec2InstanceDataRepository = ec2InstanceDataRepository;
    }

    public List<Ec2InstanceData> getAllEc2Instance(){
        return ec2InstanceDataRepository.findAll();
    }
    public Ec2InstanceData createEc3Instance(Ec2InstanceData data){
        Optional<Ec2InstanceData> existingData = ec2InstanceDataRepository.findById(data.getInstanceId());
        if(!existingData.isPresent()){
            return ec2InstanceDataRepository.save(data);
        }
        return existingData.get();

    }
}
