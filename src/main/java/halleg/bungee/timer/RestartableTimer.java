package halleg.bungee.timer;

import java.util.Timer;
import java.util.TimerTask;

public class RestartableTimer {
    private Timer timer;
    private TimerTask currentTask;
    private Runnable run;

    public RestartableTimer(Timer timer, Runnable run) {
        this.timer = timer;
        this.run = run;
    }

    public void startRepeat(long delay, long period) {
        try {
            if (this.currentTask != null) {
                this.currentTask.cancel();
            }
        } catch (IllegalStateException e) {
        }

        this.currentTask = new TimerTask() {
            @Override
            public void run() {
                RestartableTimer.this.run.run();
            }
        };
        this.timer.scheduleAtFixedRate(this.currentTask, delay, period);
    }

    public void start(long delay) {
        try {
            if (this.currentTask != null) {
                this.currentTask.cancel();
            }
        } catch (IllegalStateException e) {
        }

        this.currentTask = new TimerTask() {
            @Override
            public void run() {
                RestartableTimer.this.run.run();
            }
        };
        this.timer.schedule(this.currentTask, delay);
    }

    public void stop() {
        try {
            if (this.currentTask != null) {
                this.currentTask.cancel();
            }
        } catch (IllegalStateException e) {
        }
    }
}
