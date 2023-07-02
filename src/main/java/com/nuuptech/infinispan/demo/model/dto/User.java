package com.nuuptech.infinispan.demo.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String id;
    private String name;
    private List<String> accounts;

}
