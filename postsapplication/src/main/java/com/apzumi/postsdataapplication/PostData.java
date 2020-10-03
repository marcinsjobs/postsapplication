package com.apzumi.postsdataapplication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PostData {

    private int userId;
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String body;
}
