package io.nflow.rest.v1.springweb;

import static io.nflow.rest.config.springweb.PathConstants.NFLOW_SPRING_WEB_PATH_PREFIX;
import static io.nflow.rest.v1.ResourcePaths.NFLOW_MAINTENANCE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.nflow.engine.service.MaintenanceConfiguration;
import io.nflow.engine.service.MaintenanceResults;
import io.nflow.engine.service.MaintenanceService;
import io.nflow.rest.v1.converter.MaintenanceConverter;
import io.nflow.rest.v1.msg.MaintenanceRequest;
import io.nflow.rest.v1.msg.MaintenanceResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;

@RestController
@RequestMapping(value = NFLOW_SPRING_WEB_PATH_PREFIX + NFLOW_MAINTENANCE_PATH, produces = APPLICATION_JSON_VALUE)
@OpenAPIDefinition(info = @Info(
        title = "nFlow maintenance"
))
@Component
public class MaintenanceResource {

  @Autowired
  private MaintenanceService maintenanceService;

  @Autowired
  private MaintenanceConverter converter;

   @PostMapping(consumes = APPLICATION_JSON_VALUE)
   @Operation(description = "Do maintenance on old workflow instances synchronously")
   public MaintenanceResponse cleanupWorkflows(
           @RequestBody @Parameter(description = "Parameters for the maintenance process", required = true) MaintenanceRequest request) {
    MaintenanceConfiguration configuration = converter.convert(request);
    MaintenanceResults results = maintenanceService.cleanupWorkflows(configuration);
    return converter.convert(results);
   }
}
