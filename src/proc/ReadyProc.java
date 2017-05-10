package proc;

import log.LogSetting;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 准备阶段，计时10s
 */
public class ReadyProc extends  Proc{

    private Logger logger = LogSetting.loadSetting("准备阶段");

    /** 当前阶段的持续时间 */
    private long time=10000;

    /**
     * 等待所有玩家均准备好，向服务器传送游戏开始消息，10s后进入下一阶段
     */
    @Override
    public void run() {
        Set set = GameProc.map2.keySet();
//        while(true){
//            if(isAllReady(set))
//                break;
//        }
        for (Session s : GameProc.map1.keySet()) {
            try {
                String msg = "{\"state\": \"start\"}";
                s.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                logger.severe("向玩家返回游戏开始的消息时发生IO异常");
            }
        }
        GameProc.timer.schedule(new AssignProc(), time);
    }

    /**
     * 判断所有的HttpSession的isReady是否均为true
     * @param set
     */
//    private boolean isAllReady(Set<HttpSession> set) {
//        for (HttpSession s : set) {
//            boolean var=Boolean.parseBoolean((String)s.getAttribute("isReady"));
//            if(!var)
//                return false;
//        }
//        return true;
//    }
}
