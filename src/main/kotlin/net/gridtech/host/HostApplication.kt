package net.gridtech.host

import net.gridtech.core.util.hostInfoPublisher
import net.gridtech.host.info.HostInfoService
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@ComponentScan("net.gridtech")
@EnableMongoRepositories("net.gridtech")
class HostApplication

fun main(args: Array<String>) {
    runApplication<HostApplication>(*args)
            .apply {
                val hostInfoService: HostInfoService = getBean()
                hostInfoService.hostInfo?.apply { hostInfoPublisher.onNext(this) }
            }
}
