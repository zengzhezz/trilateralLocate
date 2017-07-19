package com.ibeacon.model.user;

import com.ibeacon.model.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zz on 2017/7/18.
 */
@Entity
@Table(name="my_user")
public class User extends IdEntity {

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
