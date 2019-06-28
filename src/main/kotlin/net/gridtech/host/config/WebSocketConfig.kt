package net.gridtech.host.config

import net.gridtech.core.util.KEY_FIELD_SECRET
import net.gridtech.host.manage.ManageService
import net.gridtech.host.service.BootService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean


@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {

    @Autowired
    lateinit var bootService: BootService
    @Autowired
    lateinit var manageService: ManageService

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean {
        val container = ServletServerContainerFactoryBean()
        container.setMaxTextMessageBufferSize(65535)
        container.setMaxBinaryMessageBufferSize(65535)
        return container
    }

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(bootService.hostMaster, "/master").setAllowedOrigins("*")
                .addInterceptors(object : HandshakeInterceptor {
                    override fun beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: MutableMap<String, Any>): Boolean {
                        val req = request as ServletServerHttpRequest
                        val resp = response as ServletServerHttpResponse
                        val nodeId = req.servletRequest.getParameter("nodeId")
                        val nodeSecret = req.servletRequest.getParameter("nodeSecret")
                        val peer = req.servletRequest.getParameter("peer")

                        if (nodeId == null || nodeSecret == null || peer == null) {
                            resp.setStatusCode(HttpStatus.FORBIDDEN)
                            return false
                        }
                        val secretFieldValue = manageService.fieldValueGet(nodeId, KEY_FIELD_SECRET)
                        return if (secretFieldValue != null && secretFieldValue.value == nodeSecret) {
                            attributes["nodeId"] = nodeId
                            attributes["peer"] = peer
                            true
                        } else {
                            resp.setStatusCode(HttpStatus.FORBIDDEN)
                            false
                        }
                    }
                    override fun afterHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, exception: Exception?) {
                    }
                })
    }
}