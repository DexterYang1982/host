package net.gridtech.host.config

import net.gridtech.core.hostInfo
import net.gridtech.core.util.ID_NODE_ROOT
import net.gridtech.core.util.KEY_FIELD_SECRET
import net.gridtech.host.manage.ManageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class WebSecurityConfig : WebMvcConfigurer {
    @Autowired
    lateinit var manageService: ManageService

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : HandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                val nodeId = request.getHeader("nodeId")
                val nodeSecret = request.getHeader("nodeSecret")
                val secretOfHostNode = hostInfo?.let { hostInfo ->
                    manageService.fieldValueGet(hostInfo.nodeId, KEY_FIELD_SECRET)
                }
                val result = (secretOfHostNode != null && secretOfHostNode.value == nodeSecret && secretOfHostNode.nodeId == nodeId) &&
                        (request.method == HttpMethod.GET.name || nodeId == ID_NODE_ROOT)
                if (!result) {
                    response.status = HttpServletResponse.SC_FORBIDDEN
                }
                return result
            }
        }).addPathPatterns("/api/**")
        super.addInterceptors(registry)
    }
}