package com.export_table_definition.infrastructure.log;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ロギング処理を行うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class Log4J2 {

    /** 唯一のLog4J2インスタンス */
    private static Log4J2 thisInstance;
    /** 改行コード */
    private final String lineSeparator = System.getProperty("line.separator");

    /**
     * インスタンス返却メソッド
     * 
     * @return Log4J2インスタンス
     */
    public synchronized static Log4J2 getInstance() {
        if (Objects.isNull(Log4J2.thisInstance)) {
            Log4J2.thisInstance = new Log4J2();
        }
        return Log4J2.thisInstance;
    }

    /**
     * コンストラクタ（インスタンス化不可）
     */
    private Log4J2() {
    }

    private String getMessage(final String msg) {
        // 自分のクラス名を取得(Log4J2)
        final String thisClassName = this.getClass().getName();
        // StackTraceElementの配列を取得
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int pos = 0;
        for (final StackTraceElement stackTraceElement : stackTraceElements) {
            // クラス名比較
            if (Objects.equals(thisClassName, stackTraceElement.getClassName())) {
                break; // stackTraceElementsから自分と同じクラス名だったら終了
            }
            pos++;
        }
        pos += 2; // 出力したいクラス名/メソッド名は自分(MyLog4J)の2個次の位置にいる
        final StackTraceElement currentStackTrace = stackTraceElements[pos];
        // ログ出力対象のクラス名:[メソッド名] + log message
        return extractClassName(currentStackTrace.getClassName()) + "#" + currentStackTrace.getMethodName() + "() "
                + msg;
    }

    /**
     * 現在のメソッドを呼び出したクラスの名前を取得するメソッド
     * 
     * @param name パッケージ名付きのクラス名
     * @return パッケージ名を削除したクラス名。取得できない場合は空文字列
     */
    private String extractClassName(String name) {
        int n = name.lastIndexOf('.');
        if (n < 0) {
            return name;
        }
        return name.substring(n + 1);
    }

    /**
     * Log4J2でデバッグレベルの情報をロギングするメソッド
     * 
     * @param msg デバッグメッセージ
     */
    public void logDebug(final String msg) {
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("{}", this.getMessage(msg));
    }

    /**
     * Log4J2でinfoレベルの情報をロギングするメソッド
     * 
     * @param msg 出力メッセージ
     */
    public void logInfo(final String msg) {
        Logger logger = LogManager.getLogger(this.getClass());
        logger.info("{}", this.getMessage(msg));
    }

    /**
     * Log4J2でinfoレベルの情報をロギングするメソッド
     * 
     * @param obj ログが出力される Class Object
     * @param msg 出力メッセージ
     */
    public void logInfo(final Object obj, final String msg) {
        Logger logger = LogManager.getLogger(obj.getClass());
        logger.info("{}", msg);
    }

    /**
     * Log4J2で警告レベルの情報をロギングするメソッド
     * 
     * @param msg 警告メッセージ
     */
    public void logWarn(final String msg) {
        Logger logger = LogManager.getLogger(this.getClass());
        logger.warn("{}", this.getMessage(msg));
    }

    /**
     * Log4J2でエラーレベル情報をロギングするメソッド
     * 
     * @param logMessage ログメッセージ
     * @param e          例外情報
     */
    public void logError(final String logMessage, final Exception e) {
        final StackTraceElement[] stackTraceElements = e.getStackTrace();
        String detailMessage = "";
        String errorlMessage = "";
        final Logger logger = LogManager.getLogger(e.getClass());
        if (Objects.nonNull(stackTraceElements) && stackTraceElements.length > 0) {
            detailMessage = lineSeparator
                    + "Class:" + e.getClass().getName() + lineSeparator
                    + "Detail:" + e.getMessage() + lineSeparator;
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                errorlMessage += stackTraceElement.toString() + lineSeparator;
            }
        }
        logger.error("{}", logMessage + detailMessage + errorlMessage);
    }

    /**
     * Log4J2でエラーレベル情報ををロギングするメソッド
     * 
     * @param e 例外情報
     */
    public void logError(final Exception e) {
        logError("", e);
    }
}