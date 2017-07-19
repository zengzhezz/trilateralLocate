package com.ibeacon.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ibeacon.service.person.UserService;
import com.ibeacon.utils.SpringContextHolder;

public class ConsumerStartListener implements ServletContextListener {


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {


    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        UserService userService = SpringContextHolder.getBean(UserService.class);
        userService.saveUser("zz", "123456");
    }

}
