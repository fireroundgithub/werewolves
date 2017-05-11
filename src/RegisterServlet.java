import entity.UserBean;
import util.UserBeanCl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by wenc on 2017/5/10.
 */
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=gbk");
        PrintWriter out = resp.getWriter();
        UserBean u = (UserBean) req.getAttribute("user");
        try {
            UserBeanCl ubc = new UserBeanCl();
            if (ubc.checkUserInfo(u)) {
                out.println("<br><br><h3>该用户名已被注册,请重新填写注册信息！</h3>");
                RequestDispatcher dispatcher = req.getRequestDispatcher("register.html");
                dispatcher.include(req, resp);
            } else {
                ubc.writeToDb(u);
                RequestDispatcher dispatcher = req.getRequestDispatcher("registerSuccess.html");
                dispatcher.include(req, resp);
            }
        } finally {
            out.close();
        }
    }
}
