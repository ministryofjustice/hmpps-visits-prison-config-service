package uk.gov.justice.digital.hmpps.visits.prison.config.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import uk.gov.justice.digital.hmpps.visits.prison.config.controller.PRISONS_PATH
import uk.gov.justice.digital.hmpps.visits.prison.config.controller.SUPPORTED_PRISONS_PATH
import uk.gov.justice.digital.hmpps.visits.prison.config.integration.helper.JwtAuthHelper
import uk.gov.justice.digital.hmpps.visits.prison.config.integration.helper.PrisonEntityHelper
import uk.gov.justice.digital.hmpps.visits.prison.config.integration.wiremock.HmppsAuthApiExtension

@ExtendWith(HmppsAuthApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  lateinit var jwtAuthHelper: JwtAuthHelper

  @Autowired
  lateinit var prisonEntityHelper: PrisonEntityHelper

  @Autowired
  protected lateinit var objectMapper: ObjectMapper

  internal fun setAuthorisation(
    user: String = "AUTH_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf(),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation(user, roles, scopes)

  fun callGet(
    webTestClient: WebTestClient,
    url: String,
    authHttpHeaders: (HttpHeaders) -> Unit,
  ): ResponseSpec {
    return webTestClient.get().uri(url)
      .headers(authHttpHeaders)
      .exchange()
  }

  fun callGetSupportedPrisons(
    webTestClient: WebTestClient,
    authHttpHeaders: (HttpHeaders) -> Unit,
  ): ResponseSpec {
    return callGet(
      webTestClient,
      url = PRISONS_PATH + SUPPORTED_PRISONS_PATH,
      authHttpHeaders,
    )
  }
}
