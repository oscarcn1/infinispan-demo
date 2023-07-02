package com.nuuptech.infinispan.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuuptech.infinispan.demo.model.dto.Permission;
import com.nuuptech.infinispan.demo.model.dto.PermissionRequest;
import com.nuuptech.infinispan.demo.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private RemoteCache<String, String> cache;
    private ObjectMapper objectMapper;

    @Autowired
    public ApiController(RemoteCacheManager cacheManager) {
        this.cache = cacheManager.getCache("usuarios");
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody User user) throws JsonProcessingException {
        String userJson = objectMapper.writeValueAsString(user);
        log.debug("Create user: {}", userJson);
        cache.put(user.getId(), userJson);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUsuer(@PathVariable String id) throws JsonProcessingException {
        String userJson = cache.get(id);
        log.debug("Get user: {}", userJson);
        User user = objectMapper.readValue(userJson, User.class);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAllUsers() throws JsonProcessingException {
        List<User> users = new ArrayList<>();
        for (Map.Entry<String, String> entry : cache.entrySet()) {
            log.debug("Fetch user: {}", entry.getValue());
            users.add(objectMapper.readValue(entry.getValue(), User.class));
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody User user) throws JsonProcessingException {
        if (cache.containsKey(user.getId())){
            String userJson = objectMapper.writeValueAsString(user);
            cache.put(user.getId(), userJson);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Delete user: {}", id);
        if (cache.containsKey(id)){
            cache.remove(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/permissionRequest")
    public ResponseEntity<Permission> permissionRequest(@RequestBody PermissionRequest permissionRequest) throws JsonProcessingException {
        if(cache.containsKey(permissionRequest.getId())) {
            User user = objectMapper.readValue(cache.get(permissionRequest.getId()), User.class);
            Permission permission = new Permission();
            permission.setId(user.getId());
            permission.setName(user.getName());
            permission.setAccount(permissionRequest.getAccount());
            permission.setCanOperate(user.getAccounts().contains(permissionRequest.getAccount()));
            return new ResponseEntity<Permission>(permission, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
