package com.apzumi.postsdataapplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

@Component
public class CyclicDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicDownloader.class);
    private final int PERIOD =  86400000; //24h

    @Autowired
    private CyclicDownloaderTimerTask cyclicDownloaderTimerTask;

    /**
     * Runs CyclicDownloaderTimerTask with a period specified in PERIOD.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runCyclicDownloader() {

        try {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatter.parse("2020-10-01 00:00:00");

            Timer timer = new Timer();
            timer.schedule(cyclicDownloaderTimerTask, date, PERIOD);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }

    }
}
