package com.nuuptech.infinispan.demo.controller;

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

    @Autowired
    private final RemoteCacheManager cacheManager;

    @PostMapping("/create")
    public ResponseEntity<Void> crearUsuario(@RequestBody User user) {
        RemoteCache<String, User> cache = cacheManager.getCache("cuentas");
        cache.put(user.getId(), user);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
