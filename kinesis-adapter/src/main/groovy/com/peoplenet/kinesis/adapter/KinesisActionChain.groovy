package com.peoplenet.kinesis.adapter

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import com.peoplenet.kinesis.adapter.handlers.HealthHandler
import com.peoplenet.kinesis.adapter.handlers.VersionHandler

import ratpack.func.Action
import ratpack.groovy.Groovy
import ratpack.handling.Chain

@CompileStatic
@Slf4j
class KinesisActionChain implements Action<Chain> {

    @Override
    void execute(Chain chain) throws Exception {
        Groovy.chain(chain) {
            get('health', HealthHandler)
            get('version', VersionHandler)
        }
    }
}
