package org.ex2_3.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LogUtil {
    private static Logger log;

    private static Logger getLoggerWrapper(String className){
        try {
            log = LogManager.getLogger(className);
        } catch (Exception e) {
            logException(e, LogUtil.class.getName());
        }
        return log;
    }

    public static void logInfo(String info, String className) {
        Logger log = getLoggerWrapper(className);
        log.info(info);
    }

    public static void logException(Exception e, String className) {
        Logger log = getLoggerWrapper(className);
        log.error(e.getMessage());
    }
}
