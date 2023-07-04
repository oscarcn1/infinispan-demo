package com.nuuptech.infinispan.demo.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class User {

    private String id;
    private String name;
    private String cardNumber01;
    private String cardNumber02;
    private String cardNumber03;
    private List<String> accounts;

}
