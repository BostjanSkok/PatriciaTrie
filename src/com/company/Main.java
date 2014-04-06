package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        Patricia<Integer> a = new Patricia<Integer>();

        a.Vstavi("test",2);
        System.out.println(a.Najdi("test"));
    }
}
