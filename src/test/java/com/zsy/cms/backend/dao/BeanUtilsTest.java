package com.zsy.cms.backend.dao;

import com.zsy.cms.backend.model.Article;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BeanUtilsTest {

    @Test
    public void beanUtilsTest01() throws Exception {

        Article a = new Article();
        /**
         *  BeanUtils.copyProperty(目标对象，属性名称，属性值);
         *  这就相当于在属性中赋予了一个值
         */
        BeanUtils.copyProperty(a, "title", "这是标题");
        System.out.println(a.getTitle());
    }

    @Test
    public void beanUtilsTest02() throws Exception {

        Article a = new Article();
        /**
         * 这里 leaveNubmer虽然是一个int类型，但是传入值是一个String
         * 赋值成功说明，其内部进行了自动的类型转换
         */
        BeanUtils.copyProperty(a, "leaveNumber", "20");
        System.out.println(a.getLeaveNumber());
    }

    @Test
    public void beanUtilsTest03() throws Exception {

        Article a = new Article();
        BeanUtils.copyProperty(a, "recommend", "true");
        System.out.println(a.isRecommend());
    }

    @Test
    public void beanUtilsTest04() throws Exception {

        Article a = new Article();
        /**
         * BeanUtils.copyProperty(a, "createTime", "2019-7-22"); 直接写报错
         * 当需要转换的值较为复杂时，例如这里用Date举例，BeanUtils需要我们提供一个转换规则
         * 否则会抛出ConversionException,提示没有默认从String到Date的转换器
         * 这时候，需要我们自己定义一个转换器，并挂载到BeanUtils上。
         * 具体就是要定义一个类，实现Converter接口
         */
        ConvertUtils.register(new DateConverter(), Date.class);

        /**
         * ConvertUtils.register(Convert convert, Class<?> clazz);
         * 通过 ConvertUtils 注册一个实现Converter接口的类，并把当要遇到需要处理的类传进去
         * 只有遇到 clazz 这种类才使用这个转换器
         */

        BeanUtils.copyProperty(a, "createTime", "2019-7-22");
        System.out.println(a.getCreateTime());
    }


    @Test
    public void beanUtilsTest05() throws Exception {

        String[] channelIds = new String[]{"1","2","3"};
        Article a = new Article();

        Set set = HashSet.class.newInstance();

        ConvertUtils.register(new ChannelConvert(), Set.class);


        BeanUtils.copyProperty(a, "channels", channelIds);
        System.out.println(a.getCreateTime());
    }

    @Test
    public void beanUtilsTest06() throws Exception {

        Article a = new Article();
        // 当传入不存在的属性的时候不会报错，只是没有赋值而已。
        BeanUtils.copyProperty(a, "name", "qwe");
        System.out.println(a);
    }

}
