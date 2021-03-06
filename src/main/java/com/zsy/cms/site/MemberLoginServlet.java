package com.zsy.cms.site;

import com.zsy.cms.backend.dao.MemberDao;
import com.zsy.cms.backend.model.Member;
import com.zsy.cms.backend.view.BaseServlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet(
        name = "MemberLoginServlet",
        urlPatterns = "/MemberLoginServlet",
        initParams = {
                @WebInitParam(name = "width", value = "44"),
                @WebInitParam(name = "height", value = "20"),
                @WebInitParam(name = "number", value = "4"),
                @WebInitParam(name = "codes", value = "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        }
)
public class MemberLoginServlet extends BaseServlet {

    private MemberDao memberDao;

    private int width;
    private int height;
    private int number; // 显示多少个字符
    private String codes; // 从哪个字符集里选择字符

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        width = Integer.parseInt(config.getInitParameter("width"));
        height = Integer.parseInt(config.getInitParameter("height"));
        number = Integer.parseInt(config.getInitParameter("number"));
        codes = config.getInitParameter("codes");

    }

    public void checkcode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("image/jpeg");

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = img.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width, height);

        Random rand = new Random();
        int x = (width-1) / number;
        int y = height-4;
        StringBuffer checkcode = new StringBuffer();
        for(int i = 0; i < number; i++) {
            String code = String.valueOf(codes.charAt(rand.nextInt(codes.length())));
            checkcode.append(code);

            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);
            Font font = new Font("Arial", Font.PLAIN, randomXtoY(height/2, height));
            g.setFont(font);
            g.setColor(new Color(red, green, blue));

            g.drawString(code, x*i + 2, y);

        }
        //随机生成一些点
        for(int i = 0; i < 20; i++) {
            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawOval(randomXtoY(3,42), randomXtoY(3, 18), 1, 1);
        }

        // 拿到response输出流
        OutputStream out = response.getOutputStream();
        // 将图片转换为JPEG类型 BufferImage 实现了RenderedImage
        boolean susses = ImageIO.write(img, "JPEG", out);
//        System.out.println(susses);
        // 将生成的验证码放在HTTP session中
        request.getSession().setAttribute("genCode", checkcode.toString());

        out.flush();
        out.close();


    }
    // 用于会员登陆
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 客户端post提交的参数
        String nickName = request.getParameter("nickName");
        String password = request.getParameter("password");
        String checkcode = request.getParameter("checkcode");

        // CheckcodeServlet 生成验证码后放入session
        String genCode = (String)request.getSession().getAttribute("genCode");

        // 判断客户端提交的checkcode和genCode是否一致
//        if(!checkcode.equalsIgnoreCase(genCode)) {
//            // 重定向到登录页面
//            request.setAttribute("error", "验证码错误");
//            request.getRequestDispatcher("/backend/login.jsp").forward(request, response);
//            return;
//        } //调试先取消验证码验证

        // 判断用户名是否存在，判断密码是否正确
        Member member = memberDao.findMemberByNickName(nickName);

        if(member == null) {
            //forward 到 login.jsp 并且提示用户名不存在
            request.setAttribute("error", "用户名【"+nickName+"】不存在");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        } else {
            if(!password.equals(member.getPassword())) {
                // forward 到 login.jsp 并提示密码不正确
                request.setAttribute("error", "用户名【"+nickName+"】密码不正确");
                request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
                return;
            }
        }

        // 全部匹配，转向后台页面
        request.getSession().setAttribute("LOGIN_MEMBER", member);
        // 当用户点击一个链接时，request中会包含有原先网页的url，并保存在header的“referer”字段中
        // 当登陆成功后我们在重定向到原来的页面
        String ref = request.getHeader("referer");
        // 判断都通过了，转向刚才发出登录请求的界面
        response.sendRedirect(ref);
    }

    public void quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 清空http session中所有参数，并把http session对象销毁
        request.getSession().invalidate();
        String ref = request.getHeader("referer");
        // 判断都通过了，转向刚才发出登录请求的界面
        response.sendRedirect(ref);
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    private int randomXtoY(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max-min) + min;
    }
}

