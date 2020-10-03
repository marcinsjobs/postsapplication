package com.apzumi.postsdataapplication;

public class WebUtils {

    static public void setSslProperties() {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
    }
}
