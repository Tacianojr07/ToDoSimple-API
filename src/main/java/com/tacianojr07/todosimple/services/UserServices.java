package com.tacianojr07.todosimple.services;

import java.util.Optional;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tacianojr07.todosimple.models.User;

import com.tacianojr07.todosimple.repositories.UserRepository;
import com.tacianojr07.todosimple.services.exceptions.DataBindingViolantionException;
import com.tacianojr07.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

   

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuario não encontrado! Id: " + id + ", tipo: " + User.class.getName()));
    }

    @Transactional
    public User createUser(User obj) {
        obj.setId(null);
        obj.setPassword(bCryptPasswordEncoder.encode(obj.getPassword()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User updateUser(User obj) {
        
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);

        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolantionException("Não é possível excluir pois há entidade relacionada");
        }
    }

}
