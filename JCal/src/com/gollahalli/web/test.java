package com.gollahalli.web;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by akshayrajgollahalli on 8/10/15.
 */
public class test {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));
//        Path currentPath = Paths.get(".");
//        System.out.println(currentPath.toAbsolutePath().toString());
//        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        test test1 = new test();
        test1.me();



    }
    public void me(){
        File f = new File(getClass().getResource(".").getPath());
        System.out.println(f.toURI());
    }
}
