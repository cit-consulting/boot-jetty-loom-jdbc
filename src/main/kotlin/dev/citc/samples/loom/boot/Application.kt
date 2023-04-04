package dev.citc.samples.loom.boot

import dev.citc.samples.loom.boot.horoscope.HoroscopeSpringConfig
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import java.util.concurrent.Executors

@SpringBootApplication
class Application : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        HoroscopeSpringConfig.initialize(applicationContext)
        beans {
            bean {
                JettyServerCustomizer { server ->
                    val threadPool = server.threadPool as QueuedThreadPool
                    threadPool.virtualThreadsExecutor = Executors.newVirtualThreadPerTaskExecutor()
                    // to force Jetty execute requests on Virtual Threads Executor
                    threadPool.reservedThreads = 0
                }
            }
            bean<ConcurrentTaskScheduler>(name = "taskScheduler", isLazyInit = true) {
                ConcurrentTaskScheduler(Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory()))
            }
        }.initialize(applicationContext)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
