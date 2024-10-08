package uk.gov.justice.digital.hmpps.visits.prison.config.integration.helper

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.visits.prison.config.entity.Prison
import uk.gov.justice.digital.hmpps.visits.prison.config.integration.repository.TestPrisonRepository
import java.time.LocalDate

@Component
@Transactional
class PrisonEntityHelper(
  private val prisonRepository: TestPrisonRepository,
) {

  companion object {
    fun createPrison(
      prisonCode: String,
      activePrison: Boolean = true,
      policyNoticeDaysMin: Int = 2,
      policyNoticeDaysMax: Int = 28,
      maxTotalVisitors: Int = 6,
      maxAdultVisitors: Int = 3,
      maxChildVisitors: Int = 3,
      adultAgeYears: Int = 18,
    ): Prison {
      return Prison(code = prisonCode, active = activePrison, policyNoticeDaysMin, policyNoticeDaysMax, maxTotalVisitors, maxAdultVisitors, maxChildVisitors, adultAgeYears)
    }
  }

  @Transactional(propagation = REQUIRES_NEW)
  fun create(
    prisonCode: String,
    activePrison: Boolean = true,
    excludeDates: List<LocalDate> = listOf(),
    policyNoticeDaysMin: Int = 2,
    policyNoticeDaysMax: Int = 28,
    dontMakeClient: Boolean = false,
  ): Prison {
    var prison = prisonRepository.findByCode(prisonCode)
    if (prison == null) {
      prison = prisonRepository.saveAndFlush(
        createPrison(
          prisonCode = prisonCode,
          activePrison = activePrison,
          policyNoticeDaysMin = policyNoticeDaysMin,
          policyNoticeDaysMax = policyNoticeDaysMax,
        ),
      )
    } else {
      prison.active = activePrison
    }
    return prison!!
  }

  @Transactional(propagation = REQUIRES_NEW)
  fun deleteAll() {
    prisonRepository.deleteAll()
    prisonRepository.flush()
  }
}
