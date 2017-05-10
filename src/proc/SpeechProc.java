package proc;

import entity.GlobalInfo;
import log.LogSetting;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 这里的发言阶段是指每位玩家的发言阶段，具体有哪些玩家发言需要依据其isDead属性，由那些isDead为false的玩家发言
 * 没人发言时长为60s，发言结束进入下一个人的发言阶段
 */
public class SpeechProc extends Proc {
    private static Logger logger = LogSetting.loadSetting("分配角色阶段");

    // 这个SpeechProc的持有者
    private Session spokesman;

    /** 当前阶段的持续时间 */
    private long time=10000;

    public SpeechProc(){

    }

    public SpeechProc(Session spokesman){
        this.spokesman = spokesman;
    }

    @Override
    public void run() {
        // 如果这是第一天，还要有请夜晚死者来发表遗言
        if (GlobalInfo.currDay == 1) {

        }

        GameProc.timer.schedule(new SpeechProc(), time);

        // 把当前发言的客户端的消息分发，其中文字消息分发到所有客户端，语音和视频消息分发到其他客户端

    }
}



/**
 * 注：这里需要GameProc去驱动voteProc
 */