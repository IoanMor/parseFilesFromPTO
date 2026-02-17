package me.ivanmorozov;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    private static String getAddress(String fileName){
        String[] strings = fileName.split("_");
        System.out.println(Arrays.toString(strings));
        for (int i = 0; i <strings.length ; i++) {
            if (strings[i].equals("д.") || strings[i].equals("д"))
            {
                return strings[i]+strings[i+1];
            } else continue;
        }
      return "Не удалось найти дом";
    }
    public static void main(String[] args) {
        System.out.println(getAddress("Троицк_мкр_В_д_49_520159_Январь_2026_ЦО.html"));
    }
}
