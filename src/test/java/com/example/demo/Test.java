package com.example.demo;

public class Test {

    public static void main(String[] args) {
        String str = "hello";
        String [] arr = str.split(",");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
