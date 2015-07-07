import com.peoplenet.kinesis.router.handlers.KinesisErrorHandler
import com.peoplenet.kinesis.router.modules.FilterModule
import com.peoplenet.kinesis.router.modules.KinesisModule
import com.peoplenet.kinesis.router.KinesisActionChain

import ratpack.config.ConfigData
import ratpack.error.ServerErrorHandler
import ratpack.jackson.JacksonModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
    serverConfig {
        port 8080
    }
    bindings {
        ConfigData configData = ConfigData.of()
                .yaml(ClassLoader.getSystemResource('application.yml'))
                .env()
                .sysProps()
                .build()
        bindInstance(ConfigData, configData)
        bindInstance(ServerErrorHandler, new KinesisErrorHandler())
        add new FilterModule(configData.get('/filter', Object))
        add new KinesisModule(configData.get('/kinesis', Object))
        add JacksonModule
    }
    handlers {
        handler chain(registry.get(KinesisActionChain))
    }
}
