package com.pedropareschi.bestfly.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
    public static String getDomain(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            return scheme + "://" + host;
        } catch (URISyntaxException e) {
            return "";
        }
    }


}
