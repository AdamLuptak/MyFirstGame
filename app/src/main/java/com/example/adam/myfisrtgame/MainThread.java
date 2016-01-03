package com.example.adam.myfisrtgame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by adam on 3.1.2016.
 */
public class MainThread extends Thread {

    private static final String TAG = "MainThread";
    private int FPS = 30;
    private double avarageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(GamePanel gamePanel, SurfaceHolder surfaceHolder) {
        super();
        this.gamePanel = gamePanel;
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        super.run();
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            //try to lock the anvas padel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //nano second
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);

            } catch (Exception e) {

            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                avarageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                Log.i(TAG, String.valueOf(avarageFPS));
            }
        }
    }

    public void setRunnning(boolean runSwitch) {
        this.running = runSwitch;
    }
}
