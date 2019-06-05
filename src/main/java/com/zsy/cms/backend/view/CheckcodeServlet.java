package com.zsy.cms.backend.view;

import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CheckcodeServlet extends HttpServlet {

    private int width;
    private int height;
    private int number; // 显示多少个字符
    private String codes; // 从哪个字符集里选择字符

    @Override
    public void init(ServletConfig config) throws ServletException {

        width = Integer.parseInt(config.getInitParameter("width"));
        height = Integer.parseInt(config.getInitParameter("height"));
        number = Integer.parseInt(config.getInitParameter("number"));
        codes = config.getInitParameter("codes");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("image/jpeg");

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
        OutputStream out = resp.getOutputStream();
        // 将图片转换为JPEG类型 BufferImage 实现了RenderedImage
        boolean susses = ImageIO.write(img, "JPEG", out);
        System.out.println(susses);
        // 将生成的验证码放在HTTP session中
        req.getSession().setAttribute("genCode", checkcode.toString());


        out.flush();
        out.close();


    }

    private int randomXtoY(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max-min) + min;
    }
}
