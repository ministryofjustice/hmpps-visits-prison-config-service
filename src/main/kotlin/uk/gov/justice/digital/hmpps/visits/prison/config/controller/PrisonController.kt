package uk.gov.justice.digital.hmpps.visits.prison.config.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.visits.prison.config.config.ErrorResponse
import uk.gov.justice.digital.hmpps.visits.prison.config.dto.PrisonDto
import uk.gov.justice.digital.hmpps.visits.prison.config.service.PrisonsService

const val PRISONS_PATH: String = "/prisons"
const val SUPPORTED_PRISONS_PATH: String = "/supported"

@RestController
@PreAuthorize("hasRole('VISIT_SCHEDULER')")
@RequestMapping(value = [PRISONS_PATH], produces = ["application/json"])
class PrisonController(
  private val prisonsService: PrisonsService,
) {
  @PreAuthorize("hasRole('VISIT_SCHEDULER')")
  @GetMapping(SUPPORTED_PRISONS_PATH)
  @Operation(
    summary = "Get all prisons",
    description = "Get all prisons",
    security = [SecurityRequirement(name = "visits-prison-config-service-client-role")],
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "prison returned",
        content = [
          Content(
            mediaType = "application/json",
            array = ArraySchema(schema = Schema(implementation = PrisonDto::class)),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Incorrect permissions to get all prisons",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun getPrisons(): List<PrisonDto> {
    return prisonsService.getPrisons()
  }
}
