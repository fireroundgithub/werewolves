import log.LogSetting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 登入游戏请求的主入口，处理请求并返回响应的Servlet
 */
public class PortalServlet extends HttpServlet {
    Logger logger = LogSetting.loadSetting("PortalServlet");

    List<HttpSession> sessionList = new ArrayList<>();

    /** 记录该玩家的编号 */
    private static int counter =0;

    /**
     * 把请求重定向到index页面
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.html");
    }

    /**
     * 准备工作:初始化Session
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession();
        int currCount = ++counter;
        s.setAttribute("num", currCount);
        s.setAttribute("isReady", false);
        sessionList.add(s);
        logger.info("Player"+currCount+" join");
        super.service(req, resp);
    }
}
