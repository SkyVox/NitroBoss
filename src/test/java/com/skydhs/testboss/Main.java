package com.skydhs.testboss;

public class Main {

    public static void main(String[] args) {
        double percentage = 10D;
        double maxHealth = 12.3;

        double result = (maxHealth*(percentage/100.0F));

        System.out.println("Percentage is: " + result + ".");
    }
}