import com.peoplenet.kinesis.adapter.config.KinesisConfiguration
import com.peoplenet.kinesis.adapter.handlers.KinesisErrorHandler
import com.peoplenet.kinesis.adapter.modules.KinesisModule
import com.peoplenet.kinesis.adapter.KinesisActionChain

import ratpack.config.ConfigData
import ratpack.error.ServerErrorHandler
import ratpack.jackson.JacksonModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
    serverConfig {
        port 8383
    }
    bindings {
        ConfigData configData = ConfigData.of()
                .yaml(ClassLoader.getSystemResource('application.yml'))
                .env()
                .sysProps()
                .build()
        bindInstance(ConfigData, configData)
        bindInstance(ServerErrorHandler, new KinesisErrorHandler())
        add new KinesisModule(configData.get('/kinesis', KinesisConfiguration))
        add JacksonModule
    }
    handlers {
        handler chain(registry.get(KinesisActionChain))
    }
}
