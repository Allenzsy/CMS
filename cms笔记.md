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
