package uk.gov.justice.digital.hmpps.visits.prison.config.integration.health

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.info.BuildProperties
import uk.gov.justice.digital.hmpps.visits.prison.config.health.HealthInfo
import uk.gov.justice.digital.hmpps.visits.prison.config.integration.IntegrationTestBase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

class InfoTest : IntegrationTestBase() {

  @Test
  fun `Info page is accessible`() {
    webTestClient.get()
      .uri("/info")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("build.name").isEqualTo("hmpps-visits-prison-config-service")
  }

  @Test
  fun `Info page reports version`() {
    webTestClient.get().uri("/info")
      .exchange()
      .expectStatus().isOk
      .expectBody().jsonPath("build.version").value<String> {
        assertThat(it).startsWith(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
      }
  }

  @Test
  fun `should include version info`() {
    val properties = Properties().apply { this.setProperty("version", "somever") }
    assertThat(HealthInfo(BuildProperties(properties)).health().details)
      .isEqualTo(mapOf("version" to "somever"))
  }
}
