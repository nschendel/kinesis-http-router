package com.peoplenet.kinesis.adapter.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Scopes
import com.google.inject.Singleton

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import com.peoplenet.kinesis.adapter.KinesisActionChain
import com.peoplenet.kinesis.adapter.config.KinesisConfiguration
import com.peoplenet.kinesis.adapter.handlers.HealthHandler
import com.peoplenet.kinesis.adapter.handlers.VersionHandler
import com.peoplenet.kinesis.adapter.services.KinesisService

@CompileStatic
@Slf4j
class KinesisModule extends AbstractModule {

    private final KinesisConfiguration kinesisConfig

    KinesisModule(KinesisConfiguration kinesisConfig) {
        this.kinesisConfig = kinesisConfig

    }

    @Override
    protected void configure() {
        bind(KinesisActionChain).in(Scopes.SINGLETON)
        bind(VersionHandler).in(Scopes.SINGLETON)
        bind(HealthHandler).in(Scopes.SINGLETON)
        bind(KinesisService).in(Scopes.SINGLETON)
    }

    @Provides
    @Singleton
    KinesisConfiguration providesKinesisConfiguration() {
        kinesisConfig
    }

}
