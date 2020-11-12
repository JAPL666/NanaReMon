package com.warma;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        HashMap<String, String> info = Login("");
        if(info!=null){
            System.out.println(getInfo(info));
        }
    }
    public static String getInfo(HashMap<String,String> map){
        String url="http://sx.lcvc.cn//mobile/training/s_practice_rz.xhtml";
        String info=POST(url, "uid="+getLuid(map)+"&pid=282&token="+getLtoken(map)+"&action=rzlist", "");
        return unicodeDecode(Regex("\"acontent\":\"([^\"]+)\"",info));
    }
    public static HashMap<String,String> Login(String account){
        String url="http://sx.lcvc.cn/mobile/common/login.xhtml";
        String str="account="+account+"&apassword="+getMD5String(account)+"&atype=2";
        String ret=POST(url,str,"");

        if(ret.contains("-2")){
            return null;
        }

        ret=ret.replace("{\"","");
        ret=ret.replace("\"}","");
        String[] rets = ret.split("\",\"");
        HashMap<String,String> map=new HashMap<>();
        for (String val:rets){
            String[] vals=val.split("\":\"");
            String key=vals[0];
            String value=vals[1];
            map.put(key,value);
        }
        return map;
    }
    public static String getLuid(HashMap<String,String> map){
        return map.get("luid");
    }
    public static String getLtoken(HashMap<String,String> map){
        return map.get("ltoken");
    }
    public static String POST(String url,String string,String Cookies) {
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
    public static String Regex(String regex,String str) {
        StringBuilder string= new StringBuilder();
        Matcher mat= Pattern.compile(regex).matcher(str);
        while(mat.find()) {
            string.append(mat.group(1)).append("\n\n\n");
        }
        return string.toString();
    }
}
