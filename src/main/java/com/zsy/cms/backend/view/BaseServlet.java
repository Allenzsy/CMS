package com.zsy.cms.backend.view;

import com.zsy.cms.SystemContext;
import com.zsy.cms.utils.BeanFactory;

import javax.servlet.ServletException;
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

        SystemContext.setOffset(this.getOffset(req));
        SystemContext.setPageSize(this.getPageSize(req));

        super.service(req, resp);

        SystemContext.removeOffset();
        SystemContext.removePageSize();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    protected void process (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        // 获取需要执行的方法
        String method = request.getParameter("method");
        System.out.println(method);
        // 这里获得的方法名，有可能是null，也有可能是空串""，这两种都应该是空。如果是具体方法则用反射执行具体方法
        if(method == null || method.trim().equals("")) {
            execute(request, response);
        } else {
            Method m = null;
            try {
                m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
                m.invoke(this, request, response);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    protected void execute (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 什么也不做，如果后面继承的类需要重写就重写，不需要就算了
    }

    protected int getOffset(HttpServletRequest request) {
        int offset = 0;
        int pageSize = 0;

        try {
            offset = Integer.parseInt(request.getParameter("pager.offset"));
        } catch (Exception ignore) { }
        return offset;
    }
    protected int getPageSize(HttpServletRequest request) {
        int pageSize = 5;
        // 希望从session中获取pageSize，session中如果没有那么则设置缺省值
        // 如果从request中拿到了pageSize的值，那么需要更新Session中的PageSize的值
        if(request.getParameter("pageSize") != null) {
            request.getSession().setAttribute("pageSize", Integer.parseInt(request.getParameter("pageSize")));
        }
        Integer ps = (Integer) request.getSession().getAttribute("pageSize");
        if(ps == null) {
            request.getSession().setAttribute("pageSize", pageSize);
        } else {
            pageSize = ps;
        }
        return pageSize;
    }
}
