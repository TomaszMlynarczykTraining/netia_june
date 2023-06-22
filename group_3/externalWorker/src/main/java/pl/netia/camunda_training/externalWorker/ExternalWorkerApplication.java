package pl.netia.camunda_training.externalWorker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.ObjectValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ExternalWorkerApplication {

	public static void main(String[] args) {
		//SpringApplication.run(ExternalWorkerApplication.class, args);
		ExternalTaskClient client = ExternalTaskClient.create()
				.baseUrl("http://localhost:8080/engine-rest")
				.build();
		Random random = new Random();

		// subscribe to the topic
		client.subscribe("getDebtData")
				.lockDuration(1000)
				.handler((externalTask, externalTaskService) -> {

					// retrieve a variable from the Process Engine
					//int defaultScore = externalTask.getVariable("defaultScore");

					Integer debtData = random.nextInt(10000);
					// create an object typed variable
					/*ObjectValue debtDataObject = Variables
							.objectValue(debtData)
							.create();

					// complete the external task
					externalTaskService.complete(externalTask,
							Collections.singletonMap("debtData", debtDataObject));
					*/
					VariableMap variables = Variables.createVariables();
					variables.put("debtData", debtData);
					externalTaskService.complete(externalTask, variables);

					System.out.println("The External Task " + externalTask.getId() + " has been completed!");

				}).open();
	}

}
