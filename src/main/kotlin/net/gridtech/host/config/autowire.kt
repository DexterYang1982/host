package net.gridtech.host.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@ComponentScan("net.gridtech")
@EnableMongoRepositories("net.gridtech")
class Autowire{
}