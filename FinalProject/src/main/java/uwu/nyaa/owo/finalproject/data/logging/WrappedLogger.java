package uwu.nyaa.owo.finalproject.data.logging;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import uwu.nyaa.owo.finalproject.system.GlobalSettings;

/**
 * A global wrapped logger
 * 
 * Makes logging relatively easy, puts everything through a single logger to a single file
 * 
 * Just use this instead of sysout and stuff will go well
 * @author minno
 *
 */
public class WrappedLogger
{
    public static FileHandler fh;

    public static ConsoleHandler ch;

    private static final Logger _LOGGER;

    static
    {
        GlobalSettings.LOG_FILE_DIRECTORY.toFile().mkdirs();

        try
        {
            ch = new ConsoleHandler();
            ch.setFormatter(new LogFormatter());

            fh = new FileHandler(Paths.get(GlobalSettings.LOG_FILE_DIRECTORY.toString(), "logs.log").toString(), true);
            fh.setFormatter(new LogFormatter()); // set formatter
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        _LOGGER = getLogger(WrappedLogger.class.getName());
    }

    public static Logger getLogger(String name)
    {
        Logger logger = Logger.getLogger(name);

        try
        {
            logger.setUseParentHandlers(false);

            logger.addHandler(ch);

            logger.addHandler(fh);

            logger.setLevel(GlobalSettings.LOG_LEVEL);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

        logger.info(name + " logger instance created");

        return logger;
    }

    public static void warning(String message)
    {
        _LOGGER.warning(message);
    }

    public static void warning(String message, Exception e)
    {
        _LOGGER.log(Level.WARNING, message, e);
    }

    public static void info(String message)
    {
        _LOGGER.info(message);
    }

    public static void log(Level logleve, String message)
    {
        _LOGGER.log(logleve, message);
    }

    public static void log(Level logleve, String message, Exception e)
    {
        _LOGGER.log(logleve, message, e);
    }
}