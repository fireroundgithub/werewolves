package log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by wenc on 2017/4/28.
 * 用于格式化log
 */
public class LogFormatter extends Formatter
{
    @Override
    public String format(LogRecord record) {
        return record.getSourceClassName() + ": "
                + record.getSourceMethodName() + ": "
                + record.getLevel() + ": " +
                record.getMessage() +
                "\n";
    }
}
