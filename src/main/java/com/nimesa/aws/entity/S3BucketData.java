package com.nimesa.aws.entity;

import com.nimesa.aws.util.StringListConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "s3bucket")
public class S3BucketData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @Id
    @Column(nullable = false)
    private String name;

    @Convert(converter = StringListConverter.class)
    private List<String> objectNames;

    private Long objCount;


}
