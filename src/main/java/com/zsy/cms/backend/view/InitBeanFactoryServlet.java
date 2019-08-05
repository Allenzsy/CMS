package com.zsy.cms.backend.view;

import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.ChannelsConverter;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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

        // 提前挂载 Converter
        ConvertUtils.register(new ChannelsConverter(), Set.class);
    }
}
