package uk.gov.justice.digital.hmpps.visits.prison.config.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
class SwaggerConfig {
  init {
    val schema = io.swagger.v3.oas.models.media.Schema<LocalTime>()
    schema.example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))).type("string").format("HH:mm").example("13:45")
    SpringDocUtils.getConfig().replaceWithSchema(LocalTime::class.java, schema)
  }
}

@Configuration
class OpenApiConfiguration(buildProperties: BuildProperties) {
  private val buildName: String = buildProperties.name
  private val buildVersion: String = buildProperties.version

  @Value("\${info.app.description}")
  private val description: String = "A microservice for administering prison configurations for hmpps-visits"

  @Value("\${info.app.contact.name}")
  private val contactName: String = "Prison Visits Booking Project"

  @Value("\${info.app.contact.email}")
  private val contactEmail: String = "prisonvisitsbooking@digital.justice.gov.uk"

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(
      listOf(
        Server().url("https://hmpps-visits-prison-config-service-dev.prison.service.justice.gov.uk").description("Development"),
        Server().url("https://hmpps-visits-prison-config-service-staging.prison.service.justice.gov.uk").description("Staging"),
        Server().url("https://hmpps-visits-prison-config-service-preprod.prison.service.justice.gov.uk").description("Pre-Production"),
        Server().url("https://hmpps-visits-prison-config-service.prison.service.justice.gov.uk").description("Production"),

        Server().url("http://localhost:8080").description("Local"),
      ),
    )
    .info(
      Info().title(buildName)
        .version(buildVersion)
        .description(description)
        .contact(Contact().name(contactName).email(contactEmail)),
    )
}
