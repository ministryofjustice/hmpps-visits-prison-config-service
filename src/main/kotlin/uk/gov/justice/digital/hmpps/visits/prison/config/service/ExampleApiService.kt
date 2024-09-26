package uk.gov.justice.digital.hmpps.visits.prison.config.service

import org.springframework.stereotype.Service
import java.time.LocalDateTime

// This is an example of how to write a service calling out to another service.  In this case we have wired up the
// kotlin template with itself so that the template doesn't depend on any other services.
// TODO: This is an example and should be renamed / replaced
@Service
class ExampleApiService {
  fun getTime(): LocalDateTime = LocalDateTime.now()
}
