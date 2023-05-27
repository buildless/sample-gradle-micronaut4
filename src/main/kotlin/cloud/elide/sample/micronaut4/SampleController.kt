package cloud.elide.sample.micronaut4

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

/** Simple string response controller. */
@Controller("/") class SampleController {
    @Get fun hello(): String = "Hello, world!"
}
