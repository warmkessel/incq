package incq.incq.enqueue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.tasks.v2.CloudTasksClient;
import com.google.cloud.tasks.v2.HttpMethod;
import com.google.cloud.tasks.v2.HttpRequest;
import com.google.cloud.tasks.v2.Task;

public class EnqueueTask {
	
    static Logger logger = Logger.getLogger(EnqueueTask.class.getName());

	public void enqueueExpandLanguageTask(String name, String[] langList, boolean force) {
        logger.log(Level.INFO, "Hit2 ");

		try (CloudTasksClient client = CloudTasksClient.create()) {
	        logger.log(Level.INFO, "Trying ");

			String parent = "projects/incq-397620/locations/us-west1/queues/Incq";

			String langListStr = String.join(",", langList);

			Task task = Task.newBuilder()
					.setHttpRequest(HttpRequest.newBuilder().setHttpMethod(HttpMethod.POST)
							.setUrl("https://incq-397620.appspot.com/tasks/expandLanguage").putHeaders("name", name)
							.putHeaders("langList", langListStr).putHeaders("force", String.valueOf(force)).build())
					.build();

			client.createTask(parent, task);
	        logger.log(Level.INFO, "finished ");

//			Task response = client.createTask(parent, task);
		} catch (Exception e) {
			// Handle exceptions
            logger.log(Level.SEVERE, "Exception: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
	}
}
