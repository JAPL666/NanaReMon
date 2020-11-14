package com.warma;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {

    public void start(String className,String methodName,String time) {

        long oneDay = 24 * 60 * 60 * 1000;
        long initDelay = getTimeMillis(time) - System.currentTimeMillis();
        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);//启用2个线程
        pool.scheduleWithFixedDelay(new Japl(className, methodName), initDelay, oneDay, TimeUnit.MILLISECONDS);

    }
    private long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    class Japl extends TimerTask {
        String className;
        String methodName;
        Japl(String className,String methodName){
            this.className=className;
            this.methodName=methodName;
        }
        @Override
        public void run() {
            try {
                Class<?> myClass = Class.forName(className);
                Object instance = myClass.getDeclaredConstructor().newInstance();
                Method method = myClass.getMethod(methodName);
                Object result = method.invoke(instance);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
