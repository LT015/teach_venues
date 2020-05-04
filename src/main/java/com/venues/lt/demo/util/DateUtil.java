package com.venues.lt.demo.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtil {

    public static int YEAR = LocalDate.now().getMonthValue() >= 9 ? LocalDate.now().getYear() : LocalDate.now().getYear() - 1;
    public static int TERM = LocalDate.now().getMonthValue() >= 9 ? 1 : 2;
    //第一周的周一
    public static LocalDate startDate = LocalDate.of(2020,02,17);

    public void getDate(){
        java.util.Date date = new java.util.Date();

        //如果希望得到YYYYMMDD的格式

        SimpleDateFormat sy1=new SimpleDateFormat("yyyy-MM-DD");

        String dateFormat=sy1.format(date);

        //如果希望分开得到年，月，日

        SimpleDateFormat sy=new SimpleDateFormat("yyyy");

        SimpleDateFormat sm=new SimpleDateFormat("MM");

        SimpleDateFormat sd=new SimpleDateFormat("dd");

        String syear=sy.format(date);

        String smon=sm.format(date);

        String sday=sd.format(date);
    }

    //根据申请日期判断是第几教学周
    public static int getWeekNum(String date) {
        String[] strings = date.split("-");
        LocalDate now = LocalDate.now();
        if(!strings[0].equals(String.valueOf(now.getYear()))){
            return 0;
        }
        LocalDate applyDate = LocalDate.of(Integer.valueOf( strings[0]), Integer.valueOf( strings[1]), Integer.valueOf( strings[2]));
        Period period = Period.between(DateUtil.startDate, applyDate);
        long aaa = startDate.until(applyDate, ChronoUnit.DAYS);
        int day = (int) aaa;//得到相差多少天
        return day / 7 + 1;
    }

    //根据申请日期判断周几
    public static int getDayOfWeek(String date) {
        String[] strings = date.split("-");
        LocalDate now = LocalDate.now();
        if(!strings[0].equals(String.valueOf(now.getYear()))){
            return 0;
        }
        LocalDate applyDate = LocalDate.of(Integer.valueOf( strings[0]), Integer.valueOf( strings[1]), Integer.valueOf( strings[2]));
        if (applyDate.getDayOfWeek() == DayOfWeek.MONDAY){
            return 1;
        }else if(applyDate.getDayOfWeek() == DayOfWeek.TUESDAY){
            return 2;
        }else if(applyDate.getDayOfWeek() == DayOfWeek.WEDNESDAY){
            return 3;
        }else if(applyDate.getDayOfWeek() == DayOfWeek.THURSDAY){
            return 4;
        }else if(applyDate.getDayOfWeek() == DayOfWeek.FRIDAY){
            return 5;
        }else if(applyDate.getDayOfWeek() == DayOfWeek.SATURDAY){
            return 6;
        }
        return 0;
    }
}
