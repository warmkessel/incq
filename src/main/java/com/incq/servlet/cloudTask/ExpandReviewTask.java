package com.incq.servlet.cloudTask;

import java.io.IOException;
//import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.incq.constants.JspConstants;
import com.incq.exception.IncqServletException;
import com.incq.instantiate.ReviewInstantiate;

//@WebServlet(name = "expandLanguageTask", urlPatterns = { "/tasks/expandLanguage" })
public class ExpandReviewTask extends HttpServlet {
    static Logger logger = Logger.getLogger(ExpandReviewTask.class.getName());

	/**
	* 
	*/
	private static final long serialVersionUID = -336146583486272075L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, IncqServletException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, IncqServletException {
		String keyString = req.getParameter(JspConstants.ID);
		String langString = req.getParameter(JspConstants.LANGUAGE);
		String stepString = req.getParameter(JspConstants.STEP);
		String contString = req.getParameter(JspConstants.CONTINUE);

		// Call your method here
		ReviewInstantiate.expandReviewSteps(keyString, langString, stepString, contString);
		
	}
}