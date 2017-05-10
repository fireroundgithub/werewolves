package proc;

import entity.GlobalInfo;
import entity.NightInfo;
import log.LogSetting;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 */
public class NightProc extends Proc{
    private static Logger logger = LogSetting.loadSetting("分配角色阶段");

    /** 当前阶段的持续时间 */
    private long time=10000;

    private NightInfo nightInfo=new NightInfo(null, false, false, 0 ,  0, 0, 0);

    @Override
    public void run() {
        // 更新当前天数
        GlobalInfo.currDay++;
        for (HttpSession var1 : GameProc.map2.keySet()) {
            Session var2;
            String msg;
            if(var1.getAttribute("role").equals("seer")){
                var2=GameProc.map2.get(var1);
                msg = "please check";
                try {
                    var2.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    logger.severe("in run()");
                }
            } else if(var1.getAttribute("role").equals("witch")){
                var2=GameProc.map2.get(var1);
                msg = "please drug";
                try {
                    var2.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    logger.severe("in run()");
                }
            } else if (var1.getAttribute("role").equals("guard")){
                var2=GameProc.map2.get(var1);
                msg = "please guard";
                try {
                    var2.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    logger.severe("in run()");
                }
            } else if (var1.getAttribute("role").equals("wolf")){
                var2=GameProc.map2.get(var1);
                msg = "please kill";
                try {
                    var2.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    logger.severe("in run()");
                }
            }
        }
        // 前40s内，预言家、守卫和狼人行动，这里主要是接收客户端消息并处理(接收消息，更新nightInfo)
        Map<Session, HttpSession> map=GameProc.map1;
        Set<Session> set = map.keySet();
        Map<Integer, String> checkRes = GameProc.nightInfo.checkRes;
        seerAct(checkRes);

        GameProc.timer.schedule(new DeathInfoProc(), time);
    }

    private void seerAct(Map<Integer, String> map){
        Iterator it = map.keySet().iterator();
        Integer var=null;
        while(it.hasNext()){
            var = (Integer) it.next();
        }
        String var3 = map.get(var);
        // 向预言家发送验人信息
        for (HttpSession var2 : GameProc.map2.keySet()) {
            if(var2.getAttribute("role").equals("seer")){
                Session seerSession = GameProc.map2.get(var2);
                String msg = var3;
                try {
                    seerSession.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    logger.severe("IoException when seerAct");
                } finally {
                    break;
                }
            }
        }
    }

}
