package com.company.gui;

public class FPSCounter extends Thread {
    private long lastTime;
    private double fps; //could be int or long for integer values

    public void run() {
        while (true) {//lazy me, add a condition for an finishable thread
            fps = 1000000000.0 / (System.nanoTime() - lastTime);//one second(nano) divided by amount of time it takes for one frame to finish
            lastTime = System.nanoTime();
        }
    }

    public double fps() {
        return fps;
    }
}
