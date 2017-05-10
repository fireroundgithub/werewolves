
import com.alibaba.fastjson.JSON;
import entity.ReadyMsg;
import log.LogSetting;
import proc.GameProc;
import proc.ReadyProc;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by wenc on 2017/4/28.
 * WebSocket连接的服务器端,所有的WebSocket消息会被该类中相关方法处理
 */
@ServerEndpoint(value = "/ws-server/{action}/{num}" ,configurator = GetHttpSessionConfigurator.class)
public class WSServer {
    public Logger logger =LogSetting.loadSetting("WS服务器端");

    public static GameProc proc;

    /** HttpSession->Session */
    public static Map<Session, HttpSession> map1 = new HashMap<>();

    /** Session->HttpSession */
    public static Map<HttpSession, Session> map2 = new HashMap<>();

    public static Set<HttpSession> httpSessions = new HashSet<>();
    public static Set<Session> sessions = new HashSet<>();

    /** 当前WSServer实例对应的HttpSession */
    HttpSession s;

    /**
     *  当WS连接建立时，该方法被调用执行
     * @param conf
     * @param session
     */
    @OnOpen
    public void onOpen( @PathParam("action") String action, EndpointConfig conf, Session session){

        s = (HttpSession) conf.getUserProperties().get(HttpSession.class.getName());
        if ("ready".equals(action)) {
            s.setAttribute("isReady", true);
        }
        logger.info(" 已经建立连接： " + s.getAttribute("num"));
        httpSessions.add(s);
        sessions.add(session);
        map1.put(session, s);
        map2.put(s, session);
        try {
            String msg = "{\"state\": \"ready\"}";
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            logger.severe(s.getAttribute("name") + " ：向客户端返回消息时发生错误");
            logger.log(Level.SEVERE, "", e);
        } finally {
            // 使用finally的意义：即使向玩家发送提示信息时出错，也能继续游戏
            // 只有当第12个玩家进入房间点击ready后，才会启动主进程
            if (sessions.size() >= 2) {
                logger.info("游戏人数已满12人");
                // 考虑到当两个玩家同时进来时，会引起多个GameProc实例，所以这里需要把GameProc设计为单例
                proc = GameProc.getInstance(map1, map2);
                proc.proc();
            }
        }
    }

    /**
     * @param msg
     */
    @OnMessage
    public void onMessage(@PathParam("action") String action, @PathParam("num") String num, String msg, Session session) {

        if("check".equals(action)){
            for (HttpSession s : map2.keySet()) {
                if (s.getAttribute("num").equals(num)){
                    String var = "nice person";
                    String role = (String) s.getAttribute("role");
                    if(role.equals("wolf"))
                        var = "wolf";
                    GameProc.nightInfo.checkRes.put((Integer) s.getAttribute("num"), var);
                    break;
                }
            }
        } else if("save".equals(action)){
            if(GameProc.nightInfo.antidote == false)
                GameProc.nightInfo.antidote =true;
        } else if ("poison".equals(action)) {
            if(GameProc.nightInfo.poison==false)
                GameProc.nightInfo.poison = true;
            GameProc.nightInfo.poisonNum = Integer.parseInt(num);
        } else if ("guard".equals(action)) {
            GameProc.nightInfo.guardNum = Integer.parseInt(num);
        } else if ("kill".equals(action)) {
            GameProc.nightInfo.killNum = Integer.parseInt(num);
        } else if("vote".equals(action)){
            int var2 = Integer.parseInt(num);
            GameProc.voteInfo.put((Integer) s.getAttribute("num"), var2);
        } else if ("chat".equals(action)) {
            Set<Session> set = session.getOpenSessions();
            for(Session var3:set){
                try {
                    var3.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    logger.severe("IOException when send msg to all client.");
                }
            }
        }
    }

    /**
     *
     */
    @OnClose
    public void onClose(Session session){

    }

    /**
     * @param e
     */
    @OnError
    public void onError(Session session ,Throwable e)  {

    }
}
