package com.tacianojr07.todosimple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


import com.tacianojr07.todosimple.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
