package com.nuuptech.infinispan.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuuptech.infinispan.demo.model.dto.User;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final RemoteCacheManager cacheManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UserController(RemoteCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> crearUsuario(@RequestBody User user) throws JsonProcessingException {
        RemoteCache<String, String> cache = cacheManager.getCache("cuentas");
        cache.put(user.getId(), objectMapper.writeValueAsString(user));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
