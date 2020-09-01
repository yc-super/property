package com.yc.property.Controller;

public class testDelete extends Thread{
    @Override
    public void run() {
        System.out.println("调用run方法");
    }
    public static void main(String []argus){
        Thread thread=new testDelete();
        thread.start();
    }
}
