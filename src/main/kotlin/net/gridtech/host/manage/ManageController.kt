package net.gridtech.host.manage

import net.gridtech.core.data.IField
import net.gridtech.core.data.IFieldValue
import net.gridtech.core.data.INode
import net.gridtech.core.data.INodeClass
import net.gridtech.repository.data.FieldValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ManageController {
    @Autowired
    lateinit var manageService: ManageService

    @RequestMapping(value = ["/nodeClassGetById"], method = [RequestMethod.GET])
    fun nodeClassGetById(@RequestParam("id")
                         id: String): ResponseEntity<INodeClass> =
            ResponseEntity.ok(manageService.nodeClassGetById(id))


    @RequestMapping(value = ["/nodeClassAdd"], method = [RequestMethod.POST])
    fun nodeClassAdd(@RequestParam("id")
                     id: String,
                     @RequestParam("name")
                     name: String,
                     @RequestParam("alias")
                     alias: String,
                     @RequestParam("connectable")
                     connectable: Boolean,
                     @RequestParam("tags", required = false)
                     tags: List<String>?,
                     @RequestBody
                     description: String): ResponseEntity<INodeClass> =
            ResponseEntity.ok(manageService.nodeClassAdd(id, name, alias, description, connectable, tags
                    ?: emptyList()))


    @RequestMapping(value = ["/nodeClassUpdate"], method = [RequestMethod.PUT])
    fun nodeClassUpdate(@RequestParam("id")
                        id: String,
                        @RequestParam("name")
                        name: String,
                        @RequestParam("alias")
                        alias: String,
                        @RequestBody
                        description: String): ResponseEntity<INodeClass> =
            ResponseEntity.ok(manageService.nodeClassUpdate(id, name, alias, description))


    @RequestMapping(value = ["/nodeClassDelete"], method = [RequestMethod.DELETE])
    fun nodeClassDelete(@RequestParam("id")
                        id: String): ResponseEntity<*> =
            ResponseEntity.ok(manageService.nodeClassDelete(id))

    @RequestMapping(value = ["/fieldGetById"], method = [RequestMethod.GET])
    fun fieldGetById(@RequestParam("id")
                     id: String): ResponseEntity<IField> =
            ResponseEntity.ok(manageService.fieldGetById(id))

    @RequestMapping(value = ["/fieldAdd"], method = [RequestMethod.POST])
    fun fieldAdd(@RequestParam("key")
                 key: String,
                 @RequestParam("nodeClassId")
                 nodeClassId: String,
                 @RequestParam("name")
                 name: String,
                 @RequestParam("alias")
                 alias: String,
                 @RequestParam("tags", required = false)
                 tags: List<String>?,
                 @RequestParam("through")
                 through: Boolean,
                 @RequestBody
                 description: String): ResponseEntity<IField> =
            ResponseEntity.ok(
                    manageService.fieldAdd(
                            key,
                            manageService.nodeClassGetById(nodeClassId),
                            name, alias, description, tags ?: emptyList(), through)
            )


    @RequestMapping(value = ["/fieldUpdate"], method = [RequestMethod.PUT])
    fun fieldUpdate(@RequestParam("id")
                    id: String,
                    @RequestParam("name")
                    name: String,
                    @RequestParam("alias")
                    alias: String,
                    @RequestBody
                    description: String): ResponseEntity<IField> =
            ResponseEntity.ok(manageService.fieldUpdate(id, name, alias, description))

    @RequestMapping(value = ["/fieldDelete"], method = [RequestMethod.DELETE])
    fun fieldDelete(@RequestParam("id")
                    id: String): ResponseEntity<*> =
            ResponseEntity.ok(manageService.fieldDelete(id))

    @RequestMapping(value = ["/nodeGetById"], method = [RequestMethod.GET])
    fun nodeGetById(@RequestParam("id")
                    id: String): ResponseEntity<*> =
            ResponseEntity.ok(manageService.nodeGetById(id))

    @RequestMapping(value = ["/nodeAdd"], method = [RequestMethod.POST])
    fun nodeAdd(@RequestParam("id")
                id: String,
                @RequestParam("nodeClassId")
                nodeClassId: String,
                @RequestParam("name")
                name: String,
                @RequestParam("alias")
                alias: String,
                @RequestParam("tags", required = false)
                tags: List<String>?,
                @RequestParam("parentId")
                parentId: String,
                @RequestParam("externalScope", required = false)
                externalScope: List<String>?,
                @RequestBody
                description: String): ResponseEntity<INode> =
            ResponseEntity.ok(
                    manageService.nodeAdd(
                            id,
                            manageService.nodeClassGetById(nodeClassId),
                            name, alias, description, tags ?: emptyList(),
                            manageService.nodeGetById(parentId),
                            externalScope ?: emptyList())
            )


    @RequestMapping(value = ["/nodeUpdate"], method = [RequestMethod.PUT])
    fun nodeUpdate(@RequestParam("id")
                   id: String,
                   @RequestParam("name")
                   name: String,
                   @RequestParam("alias")
                   alias: String,
                   @RequestBody
                   description: String): ResponseEntity<INode> =
            ResponseEntity.ok(manageService.nodeUpdate(id, name, alias, description))


    @RequestMapping(value = ["/nodeDelete"], method = [RequestMethod.DELETE])
    fun nodeDelete(@RequestParam("id")
                   id: String): ResponseEntity<*> =
            ResponseEntity.ok(manageService.nodeDelete(id))

    @RequestMapping(value = ["/fieldValueGetByFieldKey"], method = [RequestMethod.GET])
    fun fieldValueGetByFieldKey(@RequestParam("nodeId")
                                nodeId: String,
                                @RequestParam("fieldKey")
                                fieldKey: String): ResponseEntity<IFieldValue> =
            ResponseEntity.ok(manageService.fieldValueGet(nodeId, fieldKey) ?: FieldValue.empty())


    @RequestMapping(value = ["/fieldValueSetByFieldKey"], method = [RequestMethod.POST])
    fun fieldValueSetByFieldKey(@RequestParam("nodeId")
                                nodeId: String,
                                @RequestParam("fieldKey")
                                fieldKey: String,
                                @RequestParam("session")
                                session: String,
                                @RequestBody
                                value: String): ResponseEntity<*> =
            ResponseEntity.ok(manageService.fieldValueUpdate(nodeId, fieldKey, value, session))

}