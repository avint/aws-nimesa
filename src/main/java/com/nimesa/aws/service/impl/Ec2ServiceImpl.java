package com.nimesa.aws.service.impl;


import com.nimesa.aws.entity.Ec2InstanceData;
import com.nimesa.aws.repository.dao.Ec2InstanceDao;
import com.nimesa.aws.service.Ec2Service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Ec2ServiceImpl implements Ec2Service {
    private Ec2Client ec2Client;
    private Ec2InstanceDao ec2InstanceDao;
    @Autowired
    Ec2ServiceImpl(Ec2Client ec2Client, Ec2InstanceDao ec2InstanceDao){
        this.ec2Client = ec2Client;
        this.ec2InstanceDao = ec2InstanceDao;
    }

    public Ec2InstanceData createEc2InstanceData(Ec2InstanceData ec2InstanceData){
        return ec2InstanceDao.createEc3Instance(ec2InstanceData);
    }

    public List<String> getAllEc2InstanceIds(){
        List<Ec2InstanceData> data = ec2InstanceDao.getAllEc2Instance();

        return data.stream().map(Ec2InstanceData::getInstanceId).collect(Collectors.toList());
    }

    public List<Instance> getEc2Instances() {
        DescribeInstancesResponse response = ec2Client.describeInstances();
        return response.reservations().stream()
                .flatMap(reservation -> reservation.instances().stream())
                .toList();
    }


}


