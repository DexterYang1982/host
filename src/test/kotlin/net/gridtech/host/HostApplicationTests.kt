package net.gridtech.host

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class HostApplicationTests {

    @Test
    fun contextLoads() {
    }

}


abstract class A<T>(i: T) {
    var source: T = i
    fun getS(): T = source
}

class B : A<String>("hello"){
    var name="Sally"

    fun haha():A<String>? = null
}

fun main() {
    val bb=B()
    val clazz=bb::class.java

    clazz.methods
    clazz.methods.forEach {
        println(it.name)
        println(it.returnType==A::class.java)
    }

}
