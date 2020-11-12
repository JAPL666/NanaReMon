package com.warma;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Warma {
    //读取文件
    public static String read(String path){
        try {
            FileInputStream in = new FileInputStream(path);
            int lenght;
            byte[] data=new byte[1024];
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            while((lenght=in.read(data))!=-1){
                out.write(data,0,lenght);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //写文件
    public static boolean write(String path,StringBuffer str){
        try {
            FileOutputStream out=new FileOutputStream(path);
            out.write(str.toString().getBytes());
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
    //正则表达式
    public static String regex(String regex,String str) {
        StringBuilder string= new StringBuilder();
        Matcher mat= Pattern.compile(regex).matcher(str);
        while(mat.find()) {
            string.append(mat.group(1)).append("\n\n\n");
        }
        return string.toString();
    }
    public static String post(String url,String string,String Cookies) {
        try {
            URL url2=new URL(url);
            HttpURLConnection connection=(HttpURLConnection)url2.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("Pragma", "no-cache");
            connection.addRequestProperty("content-type", "application/x-www-form-urlencoded");
            connection.addRequestProperty("Accept-Language", "zh-CN,en-US;q=0.8");
            connection.addRequestProperty("Sec-Fetch-Dest","empty");
            connection.addRequestProperty("Sec-Fetch-Mode","cors");
            connection.addRequestProperty("Sec-Fetch-Site","same-site");
            connection.addRequestProperty("Cookie", Cookies);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            byte[] data=string.getBytes();
            connection.getOutputStream().write(data);
            int code=connection.getResponseCode();
            if(code==200){
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream message =new ByteArrayOutputStream();
                int length;
                byte[] buffer =new byte[1024];
                while((length=is.read(buffer))!=-1) {
                    message.write(buffer,0,length);
                }
                is.close();
                message.close();
                return new String(message.toByteArray(), StandardCharsets.UTF_8);
            }else{
                System.out.println(code);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
    public static String getMD5String(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
