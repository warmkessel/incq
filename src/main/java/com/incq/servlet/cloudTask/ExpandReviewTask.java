package com.incq.servlet.cloudTask;

import java.io.IOException;
//import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.incq.constants.JspConstants;
import com.incq.datastore.ReviewList;

//@WebServlet(name = "expandLanguageTask", urlPatterns = { "/tasks/expandLanguage" })
public class ExpandReviewTask extends HttpServlet {
    static Logger logger = Logger.getLogger(ExpandReviewTask.class.getName());

	/**
	* 
	*/
	private static final long serialVersionUID = -336146583486272075L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String keyString = req.getParameter(JspConstants.ID);
		String stepString = req.getParameter(JspConstants.STEP);
		String langString = req.getParameter(JspConstants.LANGUAGE);

		// Call your method here
		ReviewList.expandReviewSteps(keyString, langString, stepString);
		
	}
}