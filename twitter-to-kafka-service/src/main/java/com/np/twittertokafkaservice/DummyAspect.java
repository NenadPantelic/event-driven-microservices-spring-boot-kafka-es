package com.np.twittertokafkaservice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class DummyAspect {

    @Before("execution(public String getName())")
    public void getNameAdvice() {
        System.out.println("Executing Advice on getName()");
    }
}