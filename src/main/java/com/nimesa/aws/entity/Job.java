package com.nimesa.aws.entity;

import com.nimesa.aws.enums.Status;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jobs")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Status status;

    private String message;

    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    private String service;

    private String region;


}
