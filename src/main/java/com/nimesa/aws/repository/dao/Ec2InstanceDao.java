package com.nimesa.aws.repository.dao;

import com.nimesa.aws.entity.Ec2InstanceData;

import java.util.List;

public interface Ec2InstanceDao {
    public List<Ec2InstanceData> getAllEc2Instance();

    public Ec2InstanceData createEc3Instance(Ec2InstanceData data);
}
