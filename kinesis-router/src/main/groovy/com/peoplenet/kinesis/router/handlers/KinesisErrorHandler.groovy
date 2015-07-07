package com.peoplenet.kinesis.router.handlers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import io.netty.handler.codec.http.HttpResponseStatus

import ratpack.error.ServerErrorHandler
import ratpack.handling.Context

@Slf4j
@CompileStatic
class KinesisErrorHandler implements ServerErrorHandler {
    @Override
    void error(Context context, Throwable error) throws Exception {
        switch (error.class) {
            case IllegalArgumentException:
                context.response.status(HttpResponseStatus.BAD_REQUEST.code())
                context.response.send(error.message)
                break
            case IllegalStateException:
                context.response.status(HttpResponseStatus.UNAUTHORIZED.code())
                context.response.send(error.message)
                break
            case IllegalAccessException:
                context.response.status(HttpResponseStatus.UNAUTHORIZED.code())
                context.response.send(error.message)
                break
            default:
                context.response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                if (error.message) {
                    context.response.send(error.message)
                } else {
                    context.response.send("Internal Server Error - ${error.class.name}")
                }

        }
        error.printStackTrace()
    }
}

