package com.warma;

import java.io.File;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        StringBuffer str=new StringBuffer();
        StringBuffer in=new StringBuffer();
        String path="C:\\Users\\86176\\Desktop\\学号.txt";
        String[] accounts=Warma.read(path).split("\n");

        for (String account:accounts){
            account=account.trim();
            HashMap<String, String> info = Login(account);

            if(info!=null){
                String x=getInfo(info);
                str.append(account).append("\n");
                in.append(x);
                System.out.println(x);
            }else{
                System.out.println("账号："+account+"登录失败！");
            }
        }
        //保存可用账户
        Warma.write(new File(path).getParent()+"\\可用学号.txt",str);
        Warma.write(new File(path).getParent()+"\\周记.txt",in);
        System.out.println("可用账号："+str.toString().split("\n").length+"个");
        System.out.println("文件保存成功！");

    }
    public static String getInfo(HashMap<String,String> map){
        String url="http://sx.lcvc.cn//mobile/training/s_practice_rz.xhtml";
        String info=Warma.post(url, "uid="+map.get("luid")+"&pid="+map.get("pid")+"&token="+map.get("ltoken")+"&action=rzlist", "");
        return Warma.unicodeDecode(Warma.regex("\"acontent\":\"([^\"]+)\"",info));
    }
    public static HashMap<String,String> Login(String account){
        String url="http://sx.lcvc.cn/mobile/common/login.xhtml";
        String str="account="+account+"&apassword="+Warma.getMD5String(account)+"&atype=2";
        String ret=Warma.post(url,str,"");

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
        String id=Warma.post("http://sx.lcvc.cn/mobile/training/s_practice.xhtml","uid="+map.get("luid")+"&token="+map.get("ltoken"),"");
        map.put("pid",Warma.regex("\"id\":([^\"]+),",id).trim());
        return map;
    }
}
