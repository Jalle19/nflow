package io.nflow.rest.v1.springweb;

import static io.nflow.rest.config.springweb.PathConstants.NFLOW_SPRING_WEB_PATH_PREFIX;
import static io.nflow.rest.v1.ResourcePaths.NFLOW_WORKFLOW_EXECUTOR_PATH;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.nflow.engine.service.WorkflowExecutorService;
import io.nflow.rest.v1.converter.ListWorkflowExecutorConverter;
import io.nflow.rest.v1.msg.ListWorkflowExecutorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = NFLOW_SPRING_WEB_PATH_PREFIX + NFLOW_WORKFLOW_EXECUTOR_PATH, produces = APPLICATION_JSON_VALUE)
@OpenAPIDefinition(info = @Info(
        title = "nFlow workflow executor management"
))
@Component
public class WorkflowExecutorResource {

  private final WorkflowExecutorService workflowExecutors;
  private final ListWorkflowExecutorConverter converter;

  @Autowired
  public WorkflowExecutorResource(WorkflowExecutorService workflowExecutors, ListWorkflowExecutorConverter converter) {
    this.workflowExecutors = workflowExecutors;
    this.converter = converter;
  }

  @GetMapping
  @Operation(summary = "List workflow executors")
  @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ListWorkflowExecutorResponse.class))))
  public Collection<ListWorkflowExecutorResponse> listWorkflowExecutors() {
    return workflowExecutors.getWorkflowExecutors().stream().map(executor -> converter.convert(executor)).collect(toList());
  }
}
