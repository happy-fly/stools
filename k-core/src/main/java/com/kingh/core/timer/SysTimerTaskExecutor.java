package com.kingh.core.timer;

import com.kingh.config.env.EnvProperties;
import com.kingh.core.timer.task.ReloadExtLibTimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class SysTimerTaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SysTimerTaskExecutor.class);

    private static final Integer period = EnvProperties.getEnv().getExtLibReloadPeriod();

    public static void createReloadExtLibTimerTask() {
        logger.info("创建重复加载外部扩展包的定时任务开始执行...");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ReloadExtLibTimerTask(), 0, period);
    }


}
