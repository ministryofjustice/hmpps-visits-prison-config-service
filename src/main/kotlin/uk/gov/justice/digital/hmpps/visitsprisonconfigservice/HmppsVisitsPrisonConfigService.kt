package uk.gov.justice.digital.hmpps.visitsprisonconfigservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HmppsVisitsPrisonConfigService

fun main(args: Array<String>) {
  runApplication<HmppsVisitsPrisonConfigService>(*args)
}
