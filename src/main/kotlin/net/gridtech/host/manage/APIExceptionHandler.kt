package net.gridtech.host.manage

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class APIExceptionHandler {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @ExceptionHandler(APIException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    fun handleAPIException(apiException: APIException, response: HttpServletResponse): String {
        return objectMapper.writeValueAsString(apiException.getExceptionMessage())
    }
}