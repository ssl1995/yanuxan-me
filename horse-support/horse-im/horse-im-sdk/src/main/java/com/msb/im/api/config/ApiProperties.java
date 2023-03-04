package com.msb.im.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiProperties {
//    private static String baseUrl = "https://k8s-horse-gateway.mashibing.cn/im";
    private static String baseUrl = "https://you-gateway.mashibing.com/im";
//    private static String baseUrl = "http://localhost:4600/im";

    static {
        /*InputStream resourceAsStream = ApiProperties.class.getClassLoader().getResourceAsStream("config/api.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            baseUrl = properties.getProperty("baseUrl");
        } catch (IOException e) {

        }*/
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

}
