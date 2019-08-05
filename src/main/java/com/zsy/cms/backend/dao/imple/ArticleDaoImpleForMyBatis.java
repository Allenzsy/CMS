package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.SystemContext;
import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.MyBatisUtil;
import com.zsy.cms.utils.PageVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ArticleDaoImpleForMyBatis extends BaseDao implements ArticleDao {

    @Override
    public void addArticle(Article a) {
        a.setCreateTime(new Date());
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            // 插入文章
            sqlSession.insert(Article.class.getName()+".add", a);
            // 插入文章，频道关联表
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
            // 插入文章，关键字关联表
            if(a.getKeyword() != null && !a.getKeyword().trim().equals("")){
                String keyword = a.getKeyword();
                String[] keywords = keyword.split(",| ");
                for (String k : keywords) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("aid", a.getId());
                    params.put("keyword", k);
                    sqlSession.insert(Article.class.getName()+".insert_article_keyword",params);
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
        this.del(Article.class, ids);
    }

    @Override
    public PageVO<Article> findArticleByTitle(String title) {

        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        PageVO<Article> pv = this.findPaginated(Article.class.getName()+".findArticleByTitle", params);

        return pv;
    }

    @Override
    public PageVO<Article> findArticleByChannel(Channel channel) {

        Map<String, Object> params = new HashMap<>();
        params.put("c", channel);
        PageVO<Article> pv = this.findPaginated(Article.class.getName()+".findArticleByChannel", params);

        return pv;
    }

    @Override
    public Article findArticleById(String id, HttpServletRequest request, HttpServletResponse response) {

        Article article = findById(Article.class, id);

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

        return article;
    }

    @Override
    public void updateArticle(Article a) {
        a.setUpdateTime(new Date());
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            // 更新文章内容
            sqlSession.update(Article.class.getName()+".updateArticle", a);
            // 删除原来文章与频道的关联关系
            sqlSession.delete(Article.class.getName()+".delCA_Conn", a.getId());
            // 删除原来文章与关键字的关联关系
            sqlSession.delete(Article.class.getName()+".delAK_Conn", a.getId());
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
            if(a.getKeyword() != null && !a.getKeyword().trim().equals("")){
                String keyword = a.getKeyword();
                String[] keywords = keyword.split(",| ");
                for (String k : keywords) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("aid", a.getId());
                    params.put("keyword", k);
                    sqlSession.insert(Article.class.getName()+".insert_article_keyword",params);
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

    /**
     * 首页操作相关
     */
    @Override
    public List<Article> searchArticles(Channel c, int max) {
        Map<String, Object> params = new HashMap<>();
        params.put("c", c);
        SystemContext.setOffset(0);
        SystemContext.setPageSize(max);
        PageVO<Article> pv = this.findPaginated(Article.class.getName()+".findArticleByChannel", params);
        return pv.getDatas();
    }

    @Override
    public List<Article> searchHeadLine(int max) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            return sqlSession.selectList(Article.class.getName()+".searchHeadline", max);
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<Article> searchRecommend(int max) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            return sqlSession.selectList(Article.class.getName()+".searchRecommend", max);
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        return null;

    }

    @Override
    public PageVO<Article> searchArticleByKeyword(String keyword) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        if (keyword == null || keyword.trim().equals("")) {
           return null;
        }
        // 根据关键字查询到文章id
        String[] keywords = keyword.split(",| ");
        Map<String,Object> param = new HashMap<>();
        if (keywords != null && keywords.length>0) {
           StringBuffer sb = new StringBuffer();
           for (int i = 0; i < keywords.length; i++) {
               if (i != 0) {
                  sb.append(",");
               }
               sb.append("'"+keywords[i]+"'");
           }
           param.put("keyword", sb.toString());
        } else {
            return null;
        }
        List<Integer> idList= sqlSession.selectList(Article.class.getName()+".searchArticleByKeyword", param);
        // 根据文章id拿到文章
        int length = idList.size();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("'"+idList.get(i)+"'");
        }
        param.put("ids", sb.toString());
        List<Article> aList = sqlSession.selectList(Article.class.getName()+".searchArticleByIds", param);
        PageVO<Article> pv = new PageVO<>();
        pv.setDatas(aList);
        pv.setTotal(length);
        return pv;
    }

//    private void set
}
