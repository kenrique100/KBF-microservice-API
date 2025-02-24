package com.akentech.kbf.investments.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingUtil {
    public static void logInfo(String message) {
        log.info(message);
    }

    public static void logError(String message) {
        log.error(message);
    }
}