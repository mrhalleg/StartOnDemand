package halleg.tools.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer {
    private TimerCallback callback;
    private long time;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture future;
    public Timer(TimerCallback callback, long time){
        this.callback = callback;
        this.time = time;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        future = null;
    }

    public void start(){
        ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        future = exe.schedule(new Runnable() {
            @Override
            public void run() {
                timerEnded();
            }
        },time, TimeUnit.SECONDS);
    }
    public void stop(){
        if(future != null){
            future.cancel(false);
        }
    }

    private void timerEnded(){
        callback.onTimerEnd();
    }
}
