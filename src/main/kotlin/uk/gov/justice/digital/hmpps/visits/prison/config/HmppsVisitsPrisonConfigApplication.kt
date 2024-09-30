package uk.gov.justice.digital.hmpps.visits.prison.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class HmppsVisitsPrisonConfigApplication

fun main(args: Array<String>) {
  runApplication<HmppsVisitsPrisonConfigApplication>(*args)
}
