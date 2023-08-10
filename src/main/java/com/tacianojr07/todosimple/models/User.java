package com.tacianojr07.todosimple.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "User.TABLE_NAME") 
public class User {
    public static final String TABLE_NAME = "user";

}
