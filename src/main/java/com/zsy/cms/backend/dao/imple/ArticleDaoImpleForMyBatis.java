package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.MyBatisUtil;
import com.zsy.cms.utils.PageVO;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArticleDaoImpleForMyBatis implements ArticleDao {

    @Override
    public void addArticle(Article a) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            sqlSession.insert(Article.class.getName()+".add", a);

            Set<Channel> channels = a.getChannels();
            if(channels != null) {
                for (Channel c : channels) {
                    Map<String,Integer> params = new HashMap<>();
                    params.put("cid", c.getId());
                    params.put("aid", a.getId());
                    sqlSession.insert(Article.class.getName()+".insert_channel_article",params);
                    // 这里，由于文章刚刚插入，实体类中文章的id并不存在，在数据库中是存在的，是自增长的
                    // 由于设置了id作为key，所以在创建statement时，可以传入第二个参数Statement.RETURN_GENERATED_KEYS,会将自动增长的key返回到pstmt中
                }
            }

            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }


    }

    @Override
    public void delArticle(String[] ids) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            if(ids != null) {
                for (String id : ids) {
                    sqlSession.delete(Article.class.getName()+".del", Integer.parseInt(id));
                }
            }

            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public PageVO<Article> searchArticle(String title, int offset, int pageSize) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        List<Article> articles = null;
        int total = 0;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("title", title);
            params.put("offset", offset);
            params.put("pageSize", pageSize);
            articles = sqlSession.selectList(Article.class.getName()+".searchArticle",params);
            total = (Integer) sqlSession.selectOne(Article.class.getName()+".searchArticleForTotal",params);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

        PageVO<Article> pv = new PageVO<>();
        pv.setDatas(articles);
        pv.setTotal(total);


        return pv;
    }

    @Override
    public PageVO<Article> searchArticle(Channel channel, int offset, int pageSize) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        List<Article> articles = null;
        int total = 0;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("c", channel);
            params.put("offset", offset);
            params.put("pageSize", pageSize);
            articles = sqlSession.selectList(Article.class.getName()+".searchArticleByChannel",params);
            total = (Integer) sqlSession.selectOne(Article.class.getName()+".searchArticleByChannelForTotal",params);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

        PageVO<Article> pv = new PageVO<>();
        pv.setDatas(articles);
        pv.setTotal(total);


        return pv;
    }

    @Override
    public Article findArticleById(String id, HttpServletRequest request, HttpServletResponse response) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        Article article = null;
        try {
            article = sqlSession.selectOne(Article.class.getName()+".findArticleById", Integer.parseInt(id));
            if (article == null) {
                request.setAttribute("error", "不存在为"+id+"的文章，无法编辑");
                try {
                    request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        return article;
    }

    @Override
    public void updateArticle(Article a) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            // 更新文章内容
            sqlSession.update(Article.class.getName()+".updateArticle", a);
            // 删除原来文章与频道的关联关系
            sqlSession.delete(Article.class.getName()+".delCA_Conn", a.getId());
            // 设置文章新的关联关系
            Set<Channel> channels = a.getChannels();
            if(channels != null) {
                for (Channel c : channels) {
                    Map<String,Integer> params = new HashMap<>();
                    params.put("cid", c.getId());
                    params.put("aid", a.getId());
                    sqlSession.insert(Article.class.getName()+".insert_channel_article",params);
                    // 这里，由于文章刚刚插入，实体类中文章的id并不存在，在数据库中是存在的，是自增长的
                    // 由于设置了id作为key，所以在创建statement时，可以传入第二个参数Statement.RETURN_GENERATED_KEYS,会将自动增长的key返回到pstmt中
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}