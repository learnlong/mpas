package com.richong.arch.exception;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;

public class ExceptionInterceptor implements ThrowsAdvice {
	
	public void afterThrowing(Method method, Object[] args, Object target,  
            RuntimeException  throwable) {  
        System.out.println("产生异常的方法名称：  " + method.getName());  
                  
		for (Object o : args) {
			String param = "null";
			if (o != null) {
				param = o.toString();
			}
			System.out.println("方法的参数：   " + param);
		}  
          
        System.out.println("代理对象：   " + target.getClass().getName());  
        System.out.println("抛出的异常:    " + throwable.getMessage()+">>>>>>>"  
                + throwable.getCause());  
        System.out.println("异常详细信息：　　　"+throwable.fillInStackTrace());  
    }  
}
