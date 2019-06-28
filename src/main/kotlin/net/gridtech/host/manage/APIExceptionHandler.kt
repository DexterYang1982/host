package net.gridtech.host.manage

import net.gridtech.core.util.stringfy
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class APIExceptionHandler {
    @ExceptionHandler(APIException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    fun handleAPIException(apiException: APIException, response: HttpServletResponse): String {
        return stringfy(apiException.getExceptionMessage())
    }
}