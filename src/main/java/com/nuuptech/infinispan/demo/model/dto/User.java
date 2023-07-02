package com.nuuptech.infinispan.demo.model.dto;

import lombok.Data;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    @ProtoField(number = 1, required = true)
    private String id;
    @ProtoField(number = 2, required = true)
    private String name;
    @ProtoField(number = 3, collectionImplementation = ArrayList.class)
    private List<String> accounts;

    @ProtoFactory
    public User(String id, String name, List<String> accounts) {
        this.id = id;
        this.name = name;
        this.accounts = accounts;
    }
}
