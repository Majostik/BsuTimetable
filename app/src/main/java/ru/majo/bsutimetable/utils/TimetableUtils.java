package ru.majo.bsutimetable.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.model.MonthWithDays;

/**
 * Created by Majo on 24.01.2016.
 */
public class TimetableUtils {

    public static String convertType(int type){
        switch (type){
            case 0:return "Очные группы";
            case 1:return "Преподаватели";
            case 2:return "Заочные группы";
            case 3:return "Полная занятость преподавателей";
            case 4:return "Циклы";
            default:return "Полная занятость аудиторий";
        }
    }

    public static String[] getTimetableArray(Context context){
        return context.getResources().getStringArray(R.array.timetable_array);
    }

    public static String[] getTimetableArrayForLogin(Context context){
        return context.getResources().getStringArray(R.array.timetable_array_for_login);
    }

    public static String getShortDay(int i){
        switch (i){
            case 0:return "Пн";
            case 1:return "Вт";
            case 2:return "Ср";
            case 3:return "Чт";
            case 4:return "Пт";
            default:return "Сб";
        }
    }

    public static String getDays(int position){
        switch (position){
            case 0:return "Понедельник";
            case 1:return "Вторник";
            case 2:return "Среда";
            case 3:return "Четверг";
            case 4:return "Пятница";
            case 5:return "Суббота";
            default:return "Воскресенье";
        }
    }

    public static String getTime(char pair_number) {
        if (pair_number=='1')
            return "08:00";
        else if (pair_number=='2')
            return "09:40";
        else if (pair_number=='3')
            return "11:20";
        else if (pair_number=='4')
            return "13:00";
        else if (pair_number=='5')
            return "14:40";
        else if (pair_number=='6')
            return "16:20";
        else if (pair_number=='7')
            return "18:00";
        else
            return "19:40";
    }

    public static String getMonth(int i){
        switch (i){
            case 1:return "Январь";
            case 2:return "Февраль";
            case 3:return "Март";
            case 4:return "Апрель";
            case 5:return "Май";
            case 6:return "Июнь";
            case 7:return "Июль";
            case 8:return "Август";
            case 9:return "Сентябрь";
            case 10:return "Октябрь";
            case 11:return "Ноябрь";
            default:return "Декабрь";
        }
    }

    public static int getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MONTH);
    }
    
    public static MonthWithDays createObject(int month, ArrayList<String> dates){
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        ArrayList<String> resultDays = new ArrayList<>();
        for (String date: dates){
            try {
                cal.setTime(dateFormat.parse(date));
                if (month==(cal.get(Calendar.MONTH)+1))
                    resultDays.add(date);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if (!resultDays.isEmpty())
            return new MonthWithDays(month, resultDays);
        else
            return null;
    }

    public static int getParity(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = "2015-09-01";
        int startcounter = 1;
        Date date1 ,date2;
        try {
            date1 = dateFormat.parse(start);
            date2 = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date2);

            while(cal.getTime().before(cal1.getTime())) {
                startcounter+=1;
                cal.add(Calendar.DATE, 7);
            }
            int week1 = cal.get(Calendar.DAY_OF_WEEK)-1;
            int week2 = cal1.get(Calendar.DAY_OF_WEEK)-1;

            if (week1==0)
                week1=7;
            if (week2==0)
                week2=7;

            if (week1<week2)
                startcounter-=1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(startcounter%2==0)
            return 2;
        else
            return 1;
    }

    public static int getDayNumber(String date){
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDayOfWeek(String date){
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = cal.get(Calendar.DAY_OF_WEEK)-2;
        if (day<0)
            day = 6;
        return getDays(day);
    }


    public static boolean isWeekDifference(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date updateDate = dateFormat.parse(date);
            Date todayDate = new Date();
            long diffInMillies = todayDate.getTime() - updateDate.getTime();
            if (TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS)>=7)
                return true;
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getNumberDayOfWeek(String date){
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = cal.get(Calendar.DAY_OF_WEEK)-2;
        if (day<0)
            day = 6;
        return day+1;
    }


    public static boolean isTodayDate(String date){
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (dateFormat.parse(date).equals(new Date()))
                return true;
            else
                return false;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String getDayAndMonth(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(newDate);
            return cal.get(Calendar.DAY_OF_MONTH) + " " + getMonthForLesson(cal.get(Calendar.MONTH) + 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFullDayOfWeek(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(newDate);
            return getDay(cal.get(Calendar.DAY_OF_WEEK));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMonthForLesson(int i){
        switch (i){
            case 1:return "Января";
            case 2:return "Февраля";
            case 3:return "Марта";
            case 4:return "Апреля";
            case 5:return "Мая";
            case 6:return "Июня";
            case 7:return "Июля";
            case 8:return "Августа";
            case 9:return "Сентября";
            case 10:return "Октября";
            case 11:return "Ноября";
            default:return "Декабря";
        }
    }

    public static String getDay(int i){
        switch (i){
            case 1:return "Воскресенье";
            case 2:return "Понедельник";
            case 3:return "Вторник";
            case 4:return "Среда";
            case 5:return "Четверг";
            case 6:return "Пятница";
            default:return "Суббота";

        }
    }

    public static String todayDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static int todayDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day=cal.get(Calendar.DAY_OF_WEEK)-1;
        if (day==0)
            day=7;
        return day;
    }

    public static String changeDateByDay(String date,int day){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            Date convertedDate = dateFormat.parse(date);
            calendar.setTime(convertedDate);
            calendar.add(Calendar.DATE,day);
            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date addToTodayDate(String time){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return dateFormat.parse(todayDate() + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
