package com.catch32.zumpbeta.utils;

import android.util.Log;

import com.catch32.zumpbeta.constant.AppConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public final class Logger {

    public static String LOG_PREFIX = "Logger_";

    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static Boolean CONSOLE_LOG_ENABLE = AppConstant.CONSOLE_LOG_ENABLE;
    public static LogLevel CONSOLE_LOG_LEVEL = AppConstant.LOG_LEVEL;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }
        return LOG_PREFIX + str;
    }

    public enum LogLevel {
        VERBOSE(Log.VERBOSE),
        DEBUG(Log.DEBUG),
        INFO(Log.INFO),
        WARNING(Log.WARN),
        ERROR(Log.ERROR),
        ASSERT(Log.ASSERT),;

        private final int logLevel;

        LogLevel(int logLevel) {
            this.logLevel = logLevel;
        }

        public int getLogLevel() {
            return logLevel;
        }
    }


    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        write(LogLevel.VERBOSE, tag, msg);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        write(LogLevel.VERBOSE, tag, msg, tr);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        write(LogLevel.DEBUG, tag, msg);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        write(LogLevel.DEBUG, tag, msg, tr);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        write(LogLevel.INFO, tag, msg);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        write(LogLevel.INFO, tag, msg, tr);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        write(LogLevel.WARNING, tag, msg);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        write(LogLevel.WARNING, tag, msg, tr);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        write(LogLevel.ERROR, tag, msg);
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        write(LogLevel.ERROR, tag, msg, tr);
    }


    private static boolean isLogEnable(LogLevel logLevel1, LogLevel logLevel2) {
        return logLevel1.getLogLevel() <= logLevel2.getLogLevel();
    }

    private static boolean isConsoleLogEnable(LogLevel logLevel) {
        return CONSOLE_LOG_ENABLE && isLogEnable(CONSOLE_LOG_LEVEL, logLevel);
    }


    private static void write(LogLevel logLevel, String tag, String log) {

        if (isConsoleLogEnable(logLevel)) {
            println(logLevel.getLogLevel(), makeLogTag(tag), log);
        }
    }

    private static void write(LogLevel logLevel, String tag, String log, Throwable tr) {

        if (isConsoleLogEnable(logLevel)) {
            println(logLevel.getLogLevel(), makeLogTag(tag), log + "\n" + Log.getStackTraceString(tr));
        }
    }

    private static void println(int logLevel, String tag, String message) {
        int consoleLimit = 4000;
        if (message != null && message.length() > consoleLimit) {
            Log.println(logLevel, tag, "Total stack length = " + message.length());
            int chunkCount = message.length() / consoleLimit;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int start = consoleLimit * i;
                int end = consoleLimit * (i + 1);
                end = end > message.length() ? message.length() : end;
                int count = i + 1;
                int cCount = chunkCount + 1;
                Log.println(logLevel, tag, "Stack " + count + " of " + cCount + ":" + message.substring(start, end));
            }
        } else {
            Log.println(logLevel, tag, message);
        }
    }


    private static String getCurrentDateTime() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }


}