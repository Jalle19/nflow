package io.nflow.tests;

import static java.time.Duration.ofSeconds;
import static org.apache.cxf.jaxrs.client.WebClient.fromClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nflow.tests.extension.NflowServerConfig;

import io.nflow.rest.v1.msg.CreateWorkflowInstanceRequest;
import io.nflow.rest.v1.msg.CreateWorkflowInstanceResponse;
import io.nflow.rest.v1.msg.ListWorkflowInstanceResponse;
import io.nflow.tests.demo.workflow.FibonacciWorkflow;
import io.nflow.tests.extension.NflowServerExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(NflowServerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChildWorkflowTest extends AbstractNflowTest {
    public static NflowServerConfig server = new NflowServerConfig.Builder().build();

    private static int workflowId;

    public ChildWorkflowTest() {
        super(server);
    }

    @Test
    @Order(1)
    public void startFibonacciWorkflow() {
        CreateWorkflowInstanceRequest req = new CreateWorkflowInstanceRequest();
        req.type = "fibonacci";
        req.stateVariables.put("requestData", nflowObjectMapper().valueToTree(new FibonacciWorkflow.FiboData(5)));
        CreateWorkflowInstanceResponse resp = fromClient(workflowInstanceResource, true).put(req, CreateWorkflowInstanceResponse.class);
        assertThat(resp.id, notNullValue());
        workflowId = resp.id;
    }

    @Test
    @Order(2)
    public void checkFibonacciWorkflowComputesCorrectResult() throws InterruptedException {
        ListWorkflowInstanceResponse response = getWorkflowInstanceWithTimeout(workflowId, FibonacciWorkflow.State.done.name(), ofSeconds(30));
        assertTrue(response.stateVariables.containsKey("result"));
        assertEquals(8, response.stateVariables.get("result"));
    }
}