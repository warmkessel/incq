package com.incq.servlet.cloudTask;

import java.io.IOException;
//import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.incq.constants.JspConstants;
import com.incq.datastore.ReviewDetailsList;

//@WebServlet(name = "expandLanguageTask", urlPatterns = { "/tasks/expandLanguage" })
public class ExpandReviewDetailsTask extends HttpServlet {
    static Logger logger = Logger.getLogger(ExpandReviewDetailsTask.class.getName());

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
		String langString = req.getParameter(JspConstants.LANGUAGE);
		String stepString = req.getParameter(JspConstants.STEP);
		String contString = req.getParameter(JspConstants.CONTINUE);

		// Call your method here
		ReviewDetailsList.expandReviewDetailSteps(keyString, langString, stepString, contString);
		
	}
}