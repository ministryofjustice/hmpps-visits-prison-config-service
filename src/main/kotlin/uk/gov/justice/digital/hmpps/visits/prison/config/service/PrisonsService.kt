package uk.gov.justice.digital.hmpps.visits.prison.config.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.visits.prison.config.dto.PrisonDto
import uk.gov.justice.digital.hmpps.visits.prison.config.entity.Prison
import uk.gov.justice.digital.hmpps.visits.prison.config.repository.PrisonRepository

@Service
@Transactional
class PrisonsService(
  private val prisonRepository: PrisonRepository,
) {

  @Transactional(readOnly = true)
  fun getPrisons(): List<PrisonDto> {
    val prisons = prisonRepository.findAllByOrderByCodeAsc()

    return prisons.map { mapEntityToDto(it) }
  }

  fun mapEntityToDto(it: Prison): PrisonDto {
    return PrisonDto(it)
  }
}
