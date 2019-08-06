package com.zsy.cms.site;

import com.zsy.cms.backend.dao.MemberDao;
import com.zsy.cms.backend.model.Member;
import com.zsy.cms.backend.view.BaseServlet;
import com.zsy.cms.utils.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.metal.MetalMenuBarUI;
import java.io.IOException;

@WebServlet(urlPatterns = "/MemberServlet")
public class MemberServlet extends BaseServlet {

    MemberDao memberDao;
    // 打开更新会员信息页面
    public void updateInput(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        Member sessionMember = loginMember(request);
        if (sessionMember == null) {
            request.setAttribute("error", "您尚未登录无法操作");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        Member member = memberDao.findMemberById(sessionMember.getId());
        request.setAttribute("member", member);
        request.getRequestDispatcher("/member/update_input.jsp").forward(request, response);
    }
    // 更新会员信息，除了密码
    public void update(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Member sessionMember = loginMember(request);
        if (sessionMember == null) {
            request.setAttribute("error", "您尚未登录无法操作");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        Member member = RequestUtil.copyParams(Member.class, request);
        memberDao.updateMember( member);
        request.setAttribute("success", "更新个人信息成功！");
        //更新一下session中的member对象，以便在页面上能够马上看到当前登录用户显示姓名的改变
//        sessionMember.setNickName(member.getNickName());
        request.getSession().setAttribute("LOGIN_MEMBER", member);
        request.getRequestDispatcher("/common/success.jsp").forward(request, response);
    }

    /**
     * 打开修改密码页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatePasswordInput(HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {
        Member sessionMember = loginMember(request);
        if (sessionMember == null) {
            request.setAttribute("error", "您尚未登录无法操作");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        Member member = memberDao.findMemberById(sessionMember.getId());

        request.setAttribute("member", member);
        request.getRequestDispatcher("/member/update_password_input.jsp").forward(request, response);
    }

    /**
     * 修改密码后，转向成功页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatePassword(HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {
        Member sessionMember = loginMember(request);
        if (sessionMember == null) {
            request.setAttribute("error", "您尚未登录无法操作");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        String id = request.getParameter("id");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        Member member = memberDao.findMemberById(Integer.parseInt(id));

        if (!member.getPassword().equals(oldPassword)) {
            request.setAttribute("error", "您输入的旧密码不正确");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        member.setPassword(newPassword);
        memberDao.updatePassword(member);
        request.setAttribute("success", "修改密码成功，请重新登录！");
        // 清楚session，让用户重新登陆。
        request.getSession().invalidate();
        request.getRequestDispatcher("/common/success_backup.jsp").forward(request, response);
    }

    public void regInput(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,IOException{
        request.getRequestDispatcher("/member/reg_input.jsp").forward(request, response);
    }

    public void reg(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,IOException {
        Member member = RequestUtil.copyParams(Member.class, request);
        try {
            memberDao.addMember(member);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("success", "修改密码成功，请重新登录！");
        // 清楚session，让用户重新登陆。
        request.getRequestDispatcher("/common/success_backup.jsp").forward(request, response);
    }

        @Override
    protected void execute(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        Member sessionMember = loginMember(request);
        if(sessionMember == null){
            request.setAttribute("error", "您尚未登录无法操作");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        // TODO 根据登录会员，查询会员的更加详细的信息，比如上次登录的时间等等

        request.setAttribute("member", sessionMember);

        request.getRequestDispatcher("/member/index.jsp").forward(request, response);
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    protected Member loginMember(HttpServletRequest request){
        return (Member)request.getSession().getAttribute("LOGIN_MEMBER");
    }
}
