package uk.gov.justice.digital.hmpps.visits.prison.config.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.visits.prison.config.entity.Prison

@Repository
interface PrisonRepository : JpaRepository<Prison, Long> {
  fun findAllByOrderByCodeAsc(): List<Prison>
}
