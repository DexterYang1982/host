package net.gridtech.host.info

import net.gridtech.core.data.IHostInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Document
data class HostInfo(
        @Id
        var id: String = "default",
        override var nodeId: String,
        override var nodeSecret: String,
        override var parentAddress: String?
) : IHostInfo

@Repository
interface HostInfoRepository : MongoRepository<HostInfo, String> {
    fun findHostInfoById(id: String): HostInfo?
}

@Service
@ConfigurationProperties(prefix = "host")
class HostInfoService {
    var nodeId: String? = null
    var nodeSecret: String? = null
    var parentAddress: String? = null

    @Autowired
    lateinit var repository: HostInfoRepository

    @PostConstruct
    fun initInfo() {
        if (hostInfo == null && nodeId != null && nodeSecret != null) {
            hostInfo = HostInfo(
                    nodeId = nodeId!!,
                    nodeSecret = nodeSecret!!,
                    parentAddress = parentAddress
            )
        }
    }

    var hostInfo: IHostInfo?
        get() = repository.findHostInfoById("default")
        set(value) {
            if (value != null) {
                repository.save(HostInfo(
                        nodeId = value.nodeId,
                        nodeSecret = value.nodeSecret,
                        parentAddress = value.parentAddress
                ))
            }
        }
}