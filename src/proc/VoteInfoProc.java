package proc;

import log.LogSetting;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 根据投票阶段的投票信息，计算出局情况
 * 谁的票数多，就宣布谁出局
 * 平票就继续进入发言环节和投票环节，但是选项只能从平票人中选择
 * 平票再进入发言环节，若平票就进入pk台下发言环节并进行投票环节
 * 若继续平票 此轮结束，进入下一轮
 */
public class VoteInfoProc extends Proc{
    private static Logger logger = LogSetting.loadSetting("分配角色阶段");

    private Timer timer;

    /** 当前阶段的持续时间 */
    private long time=10000;

    @Override
    public void run() {
        super.run();
    }
}
