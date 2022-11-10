import it.sauronsoftware.cron4j.Scheduler;

import java.util.concurrent.Callable;

public class CronInfra {
    public Scheduler startJob(Callable<Void> callback){
        Scheduler job = new Scheduler();

        job.schedule("*/1 * * * *", () -> {
            try{
                callback.call();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        job.start();
        return job;
    }
}
