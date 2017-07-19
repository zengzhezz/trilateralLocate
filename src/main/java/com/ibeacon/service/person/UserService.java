package com.ibeacon.service.person;

import com.ibeacon.model.User.User;
import com.ibeacon.service.base.AbstractService;
import com.ibeacon.utils.SpringContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by zz on 2017/7/18.
 */
@Service
public class UserService extends AbstractService{

    public void saveUser(String username, String password){
        User user = new User(username, password);
        this.save(user);
    }

}
