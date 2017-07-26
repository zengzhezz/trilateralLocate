package com.ibeacon.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.ibeacon.model.image.ImageSize;
import com.ibeacon.model.variables.StaticVariables;
import com.ibeacon.service.image.ImageSizeService;
import com.ibeacon.thread.TrilateralThread;
import com.ibeacon.thread.WeightTrilateralThread;
import com.ibeacon.thread.YanzyAlgThread;
import com.ibeacon.utils.SpringContextHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerStartListener implements ServletContextListener {


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {


    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        ImageSizeService locationService = SpringContextHolder.getBean(
                ImageSizeService.class);
        ImageSize location = locationService.findImageSize();
        if(location!=null){
            StaticVariables.real_width = Double.parseDouble(location.getWidth());
            StaticVariables.real_height = Double.parseDouble(location.getHeight());
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new YanzyAlgThread());
    }

}
