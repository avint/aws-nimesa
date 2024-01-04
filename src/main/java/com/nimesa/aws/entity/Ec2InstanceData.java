package com.nimesa.aws.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ec2InstanceData")
public class Ec2InstanceData {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @Id
    @Column(nullable = false)
    private String instanceId;
    @Lob
    private String instanceMetaData;
}
