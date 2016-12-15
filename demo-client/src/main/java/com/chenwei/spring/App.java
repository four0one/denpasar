package com.chenwei.spring;

import com.chenwei.service.CalculateService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chenwei on 2016/12/15.
 */
public class App {

    public static void main(String[] args) {

        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:config/app-service.xml");

        CalculateService calculateService = CalculateService.class.cast(applicationContext.getBean("calculateService"));

        int result = calculateService.add(1, 2);

        System.out.println(result);

    }
}
