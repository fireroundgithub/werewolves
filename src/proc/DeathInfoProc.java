package proc;

import com.sun.tools.corba.se.idl.PragmaEntry;
import entity.DeathInfo;
import entity.GlobalInfo;
import entity.NightInfo;
import log.LogSetting;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 */
public class DeathInfoProc extends Proc {
    private static Logger logger = LogSetting.loadSetting("分配角色阶段");

    private Timer timer;

    /** 当前阶段的持续时间 */
    private long time=10000;

    DeathInfo deathInfo = new DeathInfo();

    @Override
    public void run() {
        // 根据NightProc中的NighInfo实例来计算死亡讯息，并将结果输出到DeathInfo
        this.computeInfo();
        // 将死亡讯息结果打乱，输出到每个客户端
        String msg;
        int var;
        if (deathInfo.deathNum1 == 0 && deathInfo.deathNum2 == 0) {
            msg = "{}";
            var = 0;
        } else if(deathInfo.deathNum1!=0&&deathInfo.deathNum2==0) {
            msg = "{\"death\":" + deathInfo.deathNum1 + "}";
            var = 1;
        } else {
            msg = "{\"death\":" + deathInfo.deathNum1 + "\"death\":" + deathInfo.deathNum2 + "}";
            var = 2;
        }
        for (Session s : GameProc.map1.keySet()) {
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                logger.severe("IOEXCeption");
            }
        }

        //根据死亡编号，从GameProc里的map里删除对应的Session和HttpSession
        for (HttpSession s : GameProc.map2.keySet()) {
            int num = (int) s.getAttribute("num");
            if (num == deathInfo.deathNum1 || num == deathInfo.deathNum2) {
                Session session = GameProc.map2.get(s);
                // 清除
                GameProc.map1.remove(session);
                GameProc.map2.remove(s);
            }
        }

        // 接下来进入发言阶段
        // 如果这是第一天，还要有请夜晚死者来发表遗言（暂定）
        if (GlobalInfo.currDay == -1) {
            if(var==2)
                timer.schedule(new SpeechProc(), time);
            if(var==1)
                timer.schedule(new SpeechProc(), time);
        } else{
            Set<Session> spokesmen = GameProc.map1.keySet();
            timer.schedule(new SpeechProc(), time);
        }
    }

    /**
     * 根据守卫、女巫和狼人的动作判断最终的死亡结果
     */
    private void computeInfo(){
        // 计算死亡信息
        int guard = GameProc.nightInfo.guardNum;
        int poison=GameProc.nightInfo.poisonNum;
        int save=GameProc.nightInfo.saveNum;
        int kill= GameProc.nightInfo.killNum;
        if(guard==kill&save==kill){
            deathInfo.deathNum1 = kill;
            deathInfo.deathNum2 = 0;
        } else if (guard == kill && poison != 0 && poison != kill) {
            deathInfo.deathNum1 = poison;
            deathInfo.deathNum2 = 0;
        } else if(guard==kill&&poison==0){
            deathInfo.deathNum1=0;
            deathInfo.deathNum2 = 0;
        } else if(guard!=kill&&save==kill) {
            deathInfo.deathNum1=0;
            deathInfo.deathNum2 = 0;
        } else if(guard!=kill&&poison!=kill&&poison!=0) {
            deathInfo.deathNum1 = kill;
            deathInfo.deathNum2 = poison;
        } else if (guard!=kill&&poison==0){
            deathInfo.deathNum1 = kill;
            deathInfo.deathNum2 = 0;
        }
    }
}
