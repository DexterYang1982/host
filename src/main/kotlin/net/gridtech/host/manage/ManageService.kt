package net.gridtech.host.manage

import net.gridtech.core.data.*
import net.gridtech.core.util.*
import net.gridtech.exception.APIExceptionEnum
import net.gridtech.host.service.BootService
import net.gridtech.repository.data.Field
import net.gridtech.repository.data.Node
import net.gridtech.repository.data.NodeClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class ManageService {
    @Autowired
    private lateinit var bootService: BootService

    private val nodeClassService
        get() = bootService.bootstrap.nodeClassService
    private val fieldService
        get() = bootService.bootstrap.fieldService
    private val nodeService
        get() = bootService.bootstrap.nodeService
    private val fieldValueService
        get() = bootService.bootstrap.fieldValueService

    @PostConstruct
    fun start() {
        hostInfoPublisher.subscribe { createBlankRootHost(it) }
    }


    private fun createBlankRootHost(hostInfo: IHostInfo) {
        if (hostInfo.nodeId == ID_NODE_ROOT && nodeService.getById(ID_NODE_ROOT) == null) {
            System.err.println("------init as a blank root host------")
            nodeAdd(
                    ID_NODE_ROOT,
                    nodeClassAdd(
                            ID_NODE_CLASS_ROOT,
                            "Root Node Class",
                            "root",
                            "internal",
                            true,
                            emptyList()
                    ),
                    "Root Node",
                    "root",
                    "internal",
                    emptyList(),
                    null,
                    emptyList(),
                    emptyList()
            )
            fieldValueUpdate(ID_NODE_ROOT, KEY_FIELD_SECRET, hostInfo.nodeSecret)
        }
    }

    fun nodeClassGetById(id: String): INodeClass =
            APIExceptionEnum.ERR01_ID_NOT_EXIST.assert(
                    nodeClassService.getById(id),
                    NodeClass::class, id)!!

    fun nodeClassAdd(id: String, name: String, alias: String, description: String, connectable: Boolean, tags: List<String>): INodeClass {
        APIExceptionEnum.ERR02_ID_EXISTS.assert(nodeClassService.getById(id), NodeClass::class, id)
        return NodeClass.create(
                id, name, alias, description, connectable, tags
        ).apply {
            nodeClassService.save(this)
            if (this.connectable) {
                fieldAdd(
                        KEY_FIELD_RUNNING_STATUS,
                        this,
                        "Running Status",
                        "running_status",
                        "internal field",
                        emptyList(),
                        true
                )
                fieldAdd(
                        KEY_FIELD_SECRET,
                        this,
                        "Connect Secret",
                        "connect_secret",
                        "internal field",
                        emptyList(),
                        false
                )
            }
        }
    }

    fun nodeClassUpdate(id: String, name: String, alias: String, description: String) =
            NodeClass.update(nodeClassGetById(id), name, alias, description).apply {
                nodeClassService.save(this)
            }

    fun nodeClassDelete(id: String) {
        val toDelete = nodeClassGetById(id)
        APIExceptionEnum.ERR10_CAN_NOT_BE_DELETED.assert(
                nodeService.getByNodeClass(toDelete).isEmpty(),
                NodeClass::class)
        fieldService.getByNodeClass(toDelete).forEach { fieldDelete(it.id) }
        nodeClassService.delete(id)
    }

    fun fieldGetById(id: String): IField =
            APIExceptionEnum.ERR01_ID_NOT_EXIST.assert(
                    fieldService.getById(id),
                    Field::class, id)!!

    fun fieldAdd(key: String, nodeClass: INodeClass, name: String, alias: String, description: String, tags: List<String>, through: Boolean): IField {
        val id = compose(nodeClass.id, key)
        APIExceptionEnum.ERR02_ID_EXISTS.assert(fieldService.getById(id),
                Field::class, id)
        return Field.create(
                id, nodeClass, name, alias, description, tags, through
        ).apply {
            fieldService.save(this)
        }
    }

    fun fieldUpdate(id: String, name: String, alias: String, description: String) =
            Field.update(fieldGetById(id), name, alias, description).apply {
                fieldService.save(this)
            }

    fun fieldDelete(id: String) {
        fieldService.delete(id)
    }

    fun nodeGetById(id: String): INode =
            APIExceptionEnum.ERR01_ID_NOT_EXIST.assert(
                    nodeService.getById(id),
                    Node::class,
                    id)!!

    fun nodeAdd(id: String, nodeClass: INodeClass, name: String, alias: String, description: String, tags: List<String>, parent: INode?, externalNodeIdScope: List<String>, externalNodeClassTagScope: List<String>): Node {
        APIExceptionEnum.ERR02_ID_EXISTS.assert(nodeService.getById(id),
                Node::class, id)
        APIExceptionEnum.ERR20_ROOT_NODE_IS_SINGLETON.assert(if (nodeClass.id == ID_NODE_CLASS_ROOT) id == ID_NODE_ROOT else true, Node::class, id)
        return Node.create(
                id, nodeClass, name, alias, description, tags, parent, externalNodeIdScope, externalNodeClassTagScope
        ).apply {
            nodeService.save(this)
        }
    }

    fun nodeUpdate(id: String, name: String, alias: String, description: String) =
            Node.update(nodeGetById(id), name, alias, description).apply {
                nodeService.save(this)
            }

    fun nodeDelete(id: String) {
        val toDelete = nodeGetById(id)
        APIExceptionEnum.ERR10_CAN_NOT_BE_DELETED.assert(toDelete.id != ID_NODE_ROOT,
                Node::class,
                id)
        APIExceptionEnum.ERR10_CAN_NOT_BE_DELETED.assert(nodeService.getByBranch(toDelete).isEmpty(),
                Node::class,
                id)
        nodeService.delete(id)
    }

    fun fieldValueUpdate(nodeId: String, fieldKey: String, value: String, session: String? = null) =
            fieldValueService.setFieldValueByFieldKey(nodeId, fieldKey, value, session)

    fun fieldValueGet(nodeId: String, fieldKey: String): IFieldValue? =
            fieldValueService.getFieldValueByFieldKey(nodeId, fieldKey)

}