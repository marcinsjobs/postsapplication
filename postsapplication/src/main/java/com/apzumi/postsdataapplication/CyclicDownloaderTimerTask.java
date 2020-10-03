package com.apzumi.postsdataapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class CyclicDownloaderTimerTask extends TimerTask {

    PostDataService postDataService;

    /**
     * Updates records in a database with the data from URL.
     */
    @Override
    public void run() {
        postDataService.savePostsFromApiToDb();
    }

    @Autowired
    public void setPostDataService(PostDataService postDataService) {
        this.postDataService = postDataService;
    }
}
