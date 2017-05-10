package log;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Created by wenc on 2017/4/28.
 * 用日志的设置
 */
public class LogSetting {
    private static File logFile = new File("/Users/wenc/Documents/IdeaProject/werewolves/log/process.txt");
    private static Logger logger= Logger.getLogger("日志设置");
    private static Formatter formatter=new LogFormatter();
    public static Logger loadSetting(String name){
        Logger logger2=null;
        try {
            logger2 = Logger.getLogger(name);
            logger2.setLevel(Level.INFO);
            Handler handler=new FileHandler(logFile.getAbsolutePath(), 10240, 1, true);
            handler.setFormatter(formatter);
            logger2.addHandler(handler);

        } catch (IOException e) {
            logger.info("设置日志Handler时发生IO错误");
        }
        return logger2;
    }
}
