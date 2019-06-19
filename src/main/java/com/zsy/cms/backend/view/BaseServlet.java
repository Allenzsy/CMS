package com.zsy.cms.backend.view;

import com.zsy.cms.utils.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BeanFactory beanFactory = (BeanFactory) req.getServletContext().getAttribute(InitBeanFactoryServlet.DAO_FACTORY);

        System.out.println(this);
        Class clazz = this.getClass();
        Method[] methods = clazz.getMethods();
        for(Method method:methods) {
            if(method.getName().startsWith("set")) {
                // 利用setXXX() 方法的方法名，因为XXX一般就是对象的一个属性（因为一般都是用ide直接依靠属性名生成set和get方法）
                // 那么这个属性名，一般也对应配置文件中的名字
                // 这样就相当于约定，一般认为 约定大于配置
                String propertyName = method.getName().substring(3);
                Object bean = beanFactory.getBean(propertyName);
                try {
                    method.invoke(this, bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        super.service(req, resp);
    }
}
