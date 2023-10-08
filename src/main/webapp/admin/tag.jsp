<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ page import="com.incq.datastore.*"%><%
TagManager tm = new TagManager();
%>
<%=tm.fetchUniqueTagsByAuthorString()%>
<hr>
<h1>Combined List</h1>
<%=tm.fetchUniqueTagsStringCR()%>