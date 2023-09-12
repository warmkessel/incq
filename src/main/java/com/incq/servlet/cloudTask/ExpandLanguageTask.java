package com.incq.servlet.cloudTask;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.cloud.tasks.v2.CloudTasksClient;
import com.google.cloud.tasks.v2.HttpMethod;
import com.google.cloud.tasks.v2.HttpRequest;
import com.google.cloud.tasks.v2.Task;
import com.incq.datastore.AuthorList;

@WebServlet(name = "expandLanguageTask", urlPatterns = { "/tasks/expandLanguage" })
public class ExpandLanguageTask extends HttpServlet {
    static Logger logger = Logger.getLogger(ExpandLanguageTask.class.getName());

	/**
	* 
	*/
	private static final long serialVersionUID = -336146583486272075L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.log(Level.INFO, "Hit ");
		String name = req.getParameter("name");
		String langListParam = req.getParameter("langList");
		String forceParam = req.getParameter("force");

		String[] langList = langListParam.split(","); // Assuming the list is comma-separated
		boolean force = Boolean.parseBoolean(forceParam);

		// Call your method here
        logger.log(Level.INFO, "Calling");

		AuthorList.expandLanguage(name, langList, force);
		
        logger.log(Level.INFO, "Done ");

	}
}