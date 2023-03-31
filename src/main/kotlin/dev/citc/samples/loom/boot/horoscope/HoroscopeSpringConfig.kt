package dev.citc.samples.loom.boot.horoscope

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

object HoroscopeSpringConfig : ApplicationContextInitializer<GenericApplicationContext> by beans({
    bean(::horoscopeRouter)
})
