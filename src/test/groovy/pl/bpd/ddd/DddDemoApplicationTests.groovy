package pl.bpd.ddd

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class DddDemoApplicationTests extends Specification {

    @Autowired(required = false)
    ApplicationContext applicationContext

    def "should load context"() {
        expect:
        applicationContext
    }

}
