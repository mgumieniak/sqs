package com.mgumieniak.webapp.scheduler

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.config.TriggerTask
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

@Configuration
class SchedulerTaskExecutorConfig {
    @Bean
    fun taskExecutor(): Executor {
        return Executors.newScheduledThreadPool(2)
    }
}

@EnableScheduling
@Configuration
class SchedulerConfig(
    @Qualifier("taskExecutor") private val executor: Executor,
    private val poller: Poller
) : SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(executor)
        taskRegistrar.addTriggerTask(
            TriggerTask(
                { println("Executed at: ${Instant.now()}!") },
                { Date(Instant.now().plus(poller.execAndGetNextCallTimeInSeconds(), ChronoUnit.SECONDS).toEpochMilli()) })
        )
    }
}

@Service
class Poller {

    private var nextCallInSeconds: AtomicLong = AtomicLong(10);

    fun execAndGetNextCallTimeInSeconds(): Long {
        val updateAndGet = nextCallInSeconds.updateAndGet { (5L..9L).random() }
        println("Executed at: ${Instant.now()}, next call after: $updateAndGet!")
        return updateAndGet
    }
}
