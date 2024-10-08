package uk.gov.justice.digital.hmpps.visits.prison.config.integration.prisons

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.visits.prison.config.controller.PRISONS_PATH
import uk.gov.justice.digital.hmpps.visits.prison.config.controller.SUPPORTED_PRISONS_PATH
import uk.gov.justice.digital.hmpps.visits.prison.config.dto.PrisonDto
import uk.gov.justice.digital.hmpps.visits.prison.config.enums.UserType.PUBLIC
import uk.gov.justice.digital.hmpps.visits.prison.config.enums.UserType.STAFF
import uk.gov.justice.digital.hmpps.visits.prison.config.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.visits.prison.config.repository.PrisonRepository

@Transactional
@DisplayName("Prisons $PRISONS_PATH")
class AdminPrisonsTest : IntegrationTestBase() {
  @SpyBean
  private lateinit var spyPrisonRepository: PrisonRepository

  private val adminRole = listOf("ROLE_VISIT_SCHEDULER")

  @BeforeEach
  internal fun setUpTests() {
    // We need this hear to delete the default prison in IntegrationTestBase
    prisonEntityHelper.deleteAll()
  }

  @Test
  fun `get all prisons are returned in correct order`() {
    // Given
    prisonEntityHelper.create(prisonCode = "AWE")
    prisonEntityHelper.create(prisonCode = "GRE", activePrison = false)
    prisonEntityHelper.create(prisonCode = "WDE")

    // When
    val responseSpec = webTestClient.get().uri(PRISONS_PATH + SUPPORTED_PRISONS_PATH)
      .headers(setAuthorisation(roles = adminRole))
      .exchange()

    // Then
    val results = getPrisonsResults(responseSpec)

    assertThat(results.size).isEqualTo(3)
    assertPrisonDto(results[0], prisonCode = "AWE", isActive = true)
    assertPrisonDto(results[1], prisonCode = "GRE", isActive = false)
    assertPrisonDto(results[2], prisonCode = "WDE", isActive = true)
    verify(spyPrisonRepository, times(1)).findAllByOrderByCodeAsc()
  }

  @Test
  fun `change visit - access forbidden when no role`() {
    // Given
    val incorrectAuthHeaders = setAuthorisation(roles = listOf())

    // When
    val responseSpec = callGetSupportedPrisons(webTestClient, incorrectAuthHeaders)

    // Then
    responseSpec.expectStatus().isForbidden
  }

  private fun assertPrisonDto(
    prison: PrisonDto,
    prisonCode: String,
    isActive: Boolean = true,
    isStaffActive: Boolean? = null,
    isPublicActive: Boolean? = null,
  ) {
    assertThat(prison.code).isEqualTo(prisonCode)
    assertThat(prison.active).isEqualTo(isActive)

    var expectedClientCounts = 0
    if (isStaffActive != null) expectedClientCounts++
    if (isStaffActive != null) expectedClientCounts++

    assertThat(prison.clients.size).isEqualTo(expectedClientCounts)

    if (isStaffActive != null) {
      val client = prison.clients.first { it.userType == STAFF }
      assertThat(client.userType).isEqualTo(STAFF)
      assertThat(client.active).isEqualTo(isStaffActive)
    } else {
      assertThat(prison.clients.firstOrNull { it.userType == STAFF }).isNull()
    }

    if (isPublicActive != null) {
      val client = prison.clients.first { it.userType == PUBLIC }
      assertThat(client.userType).isEqualTo(PUBLIC)
      assertThat(client.active).isEqualTo(isPublicActive)
    } else {
      assertThat(prison.clients.firstOrNull { it.userType == PUBLIC }).isNull()
    }
  }

  private fun getPrisonsResults(responseSpec: ResponseSpec): Array<PrisonDto> {
    val returnResult = responseSpec.expectStatus().isOk
      .expectBody()
    return objectMapper.readValue(returnResult.returnResult().responseBody, Array<PrisonDto>::class.java)
  }
}
