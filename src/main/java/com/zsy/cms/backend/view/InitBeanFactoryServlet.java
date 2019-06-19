package com.zsy.cms.backend.view;

import com.zsy.cms.utils.PropertiesBeanFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class InitBeanFactoryServlet extends HttpServlet {

    public static final String DAO_FACTORY = "daoFactory";

    @Override
    public void init(ServletConfig config) throws ServletException {
        String daoConfig = config.getInitParameter("daoConfig");
        PropertiesBeanFactory daoFactory = null;
        if(daoConfig == null) {
            daoFactory = new PropertiesBeanFactory();
        } else {
            daoFactory = new PropertiesBeanFactory(daoConfig);
        }
        // 如果重写了带参数的init方法，那么拿servletContext的时候，就不能通过this来拿（会产生空指针异常），而是通过config来拿
        config.getServletContext().setAttribute(DAO_FACTORY,daoFactory);

    }
}
