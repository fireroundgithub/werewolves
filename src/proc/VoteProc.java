package proc;

import log.LogSetting;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 这里是投票阶段，计时10s，接收每位玩家的投票信息，然后将结果暂时存储
 */
public class VoteProc extends Proc {
    private static Logger logger = LogSetting.loadSetting("分配角色阶段");

    /** 当前阶段的持续时间 */
    private long time=10000;
    @Override
    public void run() {
        GameProc.timer.schedule(new VoteInfoProc(), time);
    }
}
