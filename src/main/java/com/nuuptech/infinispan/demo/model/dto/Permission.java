package com.nuuptech.infinispan.demo.model.dto;

import lombok.Data;

@Data
public class Permission {

    private String id;
    private String name;
    private String account;
    private Boolean canOperate;

}
