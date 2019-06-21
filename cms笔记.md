# CMS

## login 中的 checkcode

第一步：

打开登录页面，显示验证码

![img](G:\javaTest\IdeaProjects\CMS\temp\checkcode.JPG)

浏览器通过 *get* 方法来递交请求，所以 servlet 可以只实现 `doGet` 方法。然后servlet 通过流将图片返回到 response 中。

那么思考一下，checkcode 的适用范围，是需要写死还是灵活一点，肯定是灵活一点，多少位，只显示数字，只显示英文？所以需要可配置化。对于一个servlet要实现可配置，可以实现一个 `init` 方法，把可配置信息放在 servlet 配置文件里面。

### 登录

第二步：

用户从登录页面向系统提交用户名、密码和验证码
        系统判断验证码是否正确
        系统判断用户名是否存在，判断密码是否正确

如果验证码不正确，返回登录页面，显示提示信息“验证码不正确”
        如果用户名不存在，返回登录页面，显示提示信息“用户名不存在”
        如果密码不正确，返回登录页面，显示提示信息“密码不正确”

------------
访问数据库：

注册数据库驱动
获取Connection对象(url,用户名,密码)
通过Connection对象和一条SQL语句创建Statement/PreparedStatement对象
执行SQL语句
关闭ResultSet
关闭Statement
关闭Connection

有很多请求，因为是在一个session中而不是一个http请求周期中，所以要把生成后的验证码放在session中，让其他servlet拿到

forward 发生在服务器端，两个页面拿到的request是同一个

sendredirect 重新访问一个新的页面，两个页面使用的是不同的request

但是，直接访问<http://localhost:8080/CMS_war/backend/main.jsp> 也是可以的，所以需要在登录的servlet中，在用户登录成功以后，在session中进行记录，这样当浏览器直接访问main.jsp时，如果没有登录将无法访问。



### 登出

清除session中所有参数。



### 添加文章



## 编辑文章

<center>![img](G:\javaTest\IdeaProjects\CMS\temp\编辑文章流程.JPG)</center>





将数据库操作逻辑封装到DAO中，首先实现DAO接口，然后对接口进行相应实现。使得具体的servlet不依赖于具体代码的实现。

利用抽象工厂设计模式，及使用配置文件来避免因为变化所产生的影响

DAO对象不包含状态，只是纯粹的操作，即使多个线程同时访问同一个DAO对象，也完全没有问题，
所以，没有必要每次访问DAO，都创建一个DAO对象。因此，在PropertiesBeanFactory中初始化所有的bean，每次getBean，只是直接返回一个已经初始化好的对象即可！



那么如果把PropertiesBeanFactory设计成单例模式，就可以多个Servlet共用一个DAO，但是实现的思路可以不仅仅是将PropertiesBeanFactory这个类设计成单例模式，因为单例模式实际上又使得DAO依赖于具体的实现，因为我可以从properties来创建，也可以通过xml文件来创建，我们希望获取DAO对象的时候应该只看得到BeanFactory，至于使用哪种配置文件，则去到一个统一的位置去配置好。这样的实现有很多种，那么这里采用Servlet的<load-on-startup>，在tomcat启动时就初始化DAO在之后供Servlet使用

完成PropertiesBeanFactory只创建后，我们发现再具体的Servelt使用时仍需

```java
BeanFactory factory = (BeanFactory) request.getServletContext().getAttribute(InitBeanFactoryServlet.DAO_FACTORY);
ArticleDao articleDao = (ArticleDao) factory.getBean("ArticleDao");
articleDao.addArticle(a);
```

那么实际上在servlet中，我们仍需要自己去拿需要的Dao，那么可不可以直接用呢，在Servlet中声明属性ArticleDao就可以直接使用呢？答案是使用DI注入

利用DI（Dependency Injection） - 依赖注入，将Servlet的依赖关系注入到Servlet中！实现的手段则是通过HttpServlet中的Service方法，这个方法是当HTTP请求到达servlet 时候首先调用的，简单理解就是决定了调用servlet的doGet还是doPost等等。

现在我们的Servlet都是直接继承HttpServlet，而我们需要的是Http请求在到达对应的Servlet之前将所有的Dao送到Servlet那里，所以我们再写一个BaseServlet类它继承HttpSetvlet并重写service方法，然后其他Servlet再继承BaseServlet，这样就可以实现提前将Servlet 中需要的Dao送到Servlet的效果。

那么，如何重写service方法才能实现这个效果呢？也就是说，如何在BaseServlet的service方法中，知道是哪个对象调用了service方法以及设置这个对象中的一些属性呢？这就自然想到了反射机制！

```java
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(this);//这里的this是多态的，也就是说后面继承BaseServlet的类，在HTTP到达的
        					   //时候会调用servce方法，那么这是this的运行时类型是继承的类型。
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
        super.service(req, resp);
    }
}
```

#### 减少servlet数量

把article相关的servlet都放在一个里面 ，继续利用BaseServlet，产生一个统一的调用函数，并且用反射机制去调用请求的方法。

为了减少Servlet的数量，我们设定从界面中传递一个method参数，根据这个参数的取值，利用反射机制，调用Servlet的方法，比如对于文章管理那些众多的Servlet：AddArticleServlet、DelArticlesServlet、OpenUpdateArticleServlet、UpdateArticleServlet、SearchArticlesServlet

我们将这些Servlet合成为一个Servlet：ArticleServlet，ArticleServlet的基本结构如下：
```java
public class ArticleServlet extends BaseServlet{
	//这个方法执行查询功能
	public void execute(HttpServletRequest request,HttpServletResponse response){...}
    //这个方法执行添加功能
    public void add(HttpServletRequest request,HttpServletResponse response){...}
    //这个方法执行删除功能
    public void del(HttpServletRequest request,HttpServletResponse response){...}
    //这个方法执行打开更新界面的功能
    public void updateInput(HttpServletRequest request,HttpServletResponse response){...}
    //这个方法执行更新功能
    public void update(HttpServletRequest request,HttpServletResponse response){...}
}
```
在BaseServlet中，根据界面传递过来的参数method的取值，利用反射机制调用不同的方法即可！

