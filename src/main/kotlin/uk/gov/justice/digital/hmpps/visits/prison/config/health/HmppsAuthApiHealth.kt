package uk.gov.justice.digital.hmpps.visits.prison.config.health

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.hmpps.kotlin.health.HealthPingCheck

@Component("hmppsAuthApi")
class HmppsAuthApiHealth(@Qualifier("authHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)
