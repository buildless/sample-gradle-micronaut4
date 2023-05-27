package cloud.elide.sample.micronaut4

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

/** Simple string response controller. */
@Controller("/") class SampleController {
    @Get fun hello(@QueryValue("name") name: String?): String = "Hello, ${name ?: "World"}!"
}
