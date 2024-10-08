package uk.gov.justice.digital.hmpps.visits.prison.config.integration.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.visits.prison.config.entity.Prison

@Repository
interface TestPrisonRepository : JpaRepository<Prison, Long>, JpaSpecificationExecutor<Prison> {
  fun findByCode(prisonCode: String): Prison?
}
