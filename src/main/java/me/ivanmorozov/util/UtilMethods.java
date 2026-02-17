package me.ivanmorozov.util;

public class UtilMethods {
    public static String getAddress(String fileName){
        String[] strings = fileName.split("_");
        for (int i = 0; i <strings.length ; i++) {
            if (strings[i].equals("д.") || strings[i].equals("д"))
            {
                return strings[i-2]+strings[i-1]+strings[i]+strings[i+1];
            } else continue;
        }
        return "Не удалось найти дом";
    }
}
