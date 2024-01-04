package com.nimesa.aws.service;

import com.nimesa.aws.entity.Ec2InstanceData;
import software.amazon.awssdk.services.ec2.model.Instance;

import java.util.List;

public interface Ec2Service {
     Ec2InstanceData createEc2InstanceData(Ec2InstanceData ec2InstanceData);
     List<String> getAllEc2InstanceIds();
     List<Instance> getEc2Instances();

}
