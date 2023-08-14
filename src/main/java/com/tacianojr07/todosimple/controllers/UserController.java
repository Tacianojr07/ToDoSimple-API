package com.tacianojr07.todosimple.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tacianojr07.todosimple.models.User;
import com.tacianojr07.todosimple.models.User.CreateUser;
import com.tacianojr07.todosimple.models.User.UpdateUser;
import com.tacianojr07.todosimple.services.UserServices;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    
    @Autowired
    private UserServices userServices;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById( @PathVariable Long id){
        User user = this.userServices.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @Validated(CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody User obj) {
        this.userServices.createUser(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(Update.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User obj, @PathVariable Long id) {
        obj.setId(id);
        this.userServices.updateUser(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userServices.delete(id);
        return ResponseEntity.noContent().build();
    }



}
