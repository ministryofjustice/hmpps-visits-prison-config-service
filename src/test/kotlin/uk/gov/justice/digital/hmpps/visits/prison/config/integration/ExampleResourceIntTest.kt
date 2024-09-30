package uk.gov.justice.digital.hmpps.visits.prison.config.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ExampleResourceIntTest : IntegrationTestBase() {

  @Nested
  @DisplayName("GET /example/time")
  inner class TimeEndpoint {

    @Test
    fun `should return unauthorized if no token`() {
      webTestClient.get()
        .uri("/example/time")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return forbidden if no role`() {
      webTestClient.get()
        .uri("/example/time")
        .headers(jwtAuthHelper.setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return forbidden if wrong role`() {
      webTestClient.get()
        .uri("/example/time")
        .headers(jwtAuthHelper.setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return OK`() {
      webTestClient.get()
        .uri("/example/time")
        .headers(jwtAuthHelper.setAuthorisation(roles = listOf("ROLE_VISIT_SCHEDULER")))
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .jsonPath("$").value<String> {
          assertThat(it).startsWith("${LocalDate.now()}")
        }
    }
  }
}
