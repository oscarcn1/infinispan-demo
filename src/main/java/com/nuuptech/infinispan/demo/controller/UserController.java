package com.nuuptech.infinispan.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuuptech.infinispan.demo.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public ResponseEntity<Void> createUser(@RequestBody User user) throws JsonProcessingException {
        RemoteCache<String, String> cache = cacheManager.getCache("usuarios");
        String userJson = objectMapper.writeValueAsString(user);
        log.info("User: {}", userJson);
        cache.put(user.getId(), userJson);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUsuer(@PathVariable String id) throws JsonProcessingException {
        RemoteCache<String, String> cache = cacheManager.getCache("usuarios");
        String userJson = cache.get(id);
        User user = objectMapper.readValue(userJson, User.class);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
