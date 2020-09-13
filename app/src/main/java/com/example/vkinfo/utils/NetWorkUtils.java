package com.example.vkinfo.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetWorkUtils {
   private static final String VK_API_BASE_URL = "https://api.vk.com";
   private static final String VK_USERS_GET = "/method/users.get";
   private static final String PARAM_USER_ID = "user_ids";
   private static final String PARAM_VERSION = "v";
   private static final String ACCESS_TOKEN = "access_token";
    public static URL generateURL(String userIds){
        Uri builtUri = Uri.parse(VK_API_BASE_URL+ VK_USERS_GET).buildUpon()
                .appendQueryParameter(PARAM_USER_ID, userIds)
                .appendQueryParameter(PARAM_VERSION, "5.81")
                .appendQueryParameter(ACCESS_TOKEN, "1b266af81b266af81b266af8351b4de3e211b261b266af84607b14b1ce7b15d5d27b0ce")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }
        catch (UnknownHostException e){
           return null;
        }  finally{
            urlConnection.disconnect();
        }



    }

}
