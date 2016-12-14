package ch.unibe.ese.team1.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * This class is responsible for the logger setup. It sets the global/root logger level to warning 
 * and creates a filehandler.
 *
 */

public class LogMain {

    public Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public LogMain() {
        Logger root = Logger.getLogger("");
        FileHandler filehandler = null;
        // exception only when there are no write permissions, difficult to test
        try {
            filehandler = new FileHandler("controller.log");
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        root.setLevel(Level.WARNING);
        filehandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                String ret = "";
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Date d = new Date(record.getMillis());
                ret += df.format(d);
                ret += " ";
                ret += this.formatMessage(record);
                ret += "\r\n";
                return ret;
            }
        });
        root.addHandler(filehandler);
    }
}
