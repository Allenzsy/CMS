package com.zsy.cms.utils;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.ChannelDao;

public interface BeanFactory {

    public Object getBean(String name);
}
