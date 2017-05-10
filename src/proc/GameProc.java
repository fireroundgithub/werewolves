package proc;

import entity.NightInfo;
import entity.VoteInfo;
import log.LogSetting;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 这个是游戏的主线程，推动着游戏的进度
 */
public class GameProc {
    private static Logger logger = LogSetting.loadSetting("分配角色阶段");

    private static GameProc proc = null;
    private static Lock lock = new ReentrantLock();

    /**
     * 游戏的定时器
     */
    static Timer timer;

    /**
     * Session<->HttpSession
     */
    static Map<Session, HttpSession> map1;
    static Map<HttpSession, Session> map2;

    public static NightInfo nightInfo = new NightInfo();
    public static VoteInfo voteInfo = new VoteInfo();

    private GameProc(Map map1, Map map2) {
        this.map1 = map1;
        this.map2 = map2;
    }

    /**
     * 游戏的主线程主函数,推动游戏的发展。
     * 把游戏分为多个阶段，用多个proc来处理,依次为<code>ReadyProc</code>,<code>AssignProc</code>,<code>NighProc</code>,
     * <code>DeathInfoProc</code>,若干个<Speech>SpeechProc</Speech>,<code>VoteProc</code>,<code>VoteInfoProc</code>,
     * <code>VoteInfoProc</code>
     */
    public void proc() {
        ReadyProc readyProc = new ReadyProc();
        readyProc.run();
    }


    /**
     * 获取GameProc实例
     * @return
     */
    public static GameProc getInstance(Map map1, Map map2) {
        if (proc != null) {
            return proc;
        }
        lock.lock();
        if (proc == null) {
            proc = new GameProc(map1, map2);
        }
        lock.unlock();
        return proc;
    }
}
