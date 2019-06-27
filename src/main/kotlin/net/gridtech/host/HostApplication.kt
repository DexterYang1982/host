package net.gridtech.host

import net.gridtech.core.util.hostInfoPublisher
import net.gridtech.host.info.HostInfoService
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HostApplication

fun main(args: Array<String>) {
    runApplication<HostApplication>(*args)
            .apply {
                val hostInfoService: HostInfoService = getBean()
                hostInfoService.hostInfo?.apply { hostInfoPublisher.onNext(this) }
            }
}
