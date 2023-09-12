package com.incq.filter;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
//import com.shrineoflostsecrets.ai.AIImageManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class MemcacheFilter implements Filter {
	private static final Logger log = Logger.getLogger(MemcacheFilter.class.getName());

    private MemcacheService memcacheService;
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) {
        memcacheService = MemcacheServiceFactory.getMemcacheService();
        userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        User currentUser = userService.getCurrentUser();

        String cacheKey = httpRequest.getRequestURI() + "?" + httpRequest.getQueryString();
        if (currentUser != null) {
            cacheKey += "&email=" + currentUser.getEmail();
        }
		log.info("cacheKey:" + cacheKey);

        boolean noCache = httpRequest.getParameter("nocache") != null;
        boolean flushCache = httpRequest.getParameter("flushcache") != null;

        if (flushCache) {
            memcacheService.clearAll();
        }

        byte[] cachedResponse = noCache ? null : (byte[]) memcacheService.get(cacheKey);

        if (cachedResponse == null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(httpResponse) {
                @Override
                public javax.servlet.ServletOutputStream getOutputStream() {
                    return new javax.servlet.ServletOutputStream() {
                        @Override
                        public void write(int b) throws IOException {
                            outputStream.write(b);
                        }

                        @Override
                        public boolean isReady() {
                            return true;
                        }

                        @Override
                        public void setWriteListener(WriteListener writeListener) {
                            // No need to implement this method since the output stream is synchronous
                        }
                    };
                }

                @Override
                public PrintWriter getWriter() {
                    return printWriter;
                }
            };

            chain.doFilter(request, responseWrapper);
            printWriter.flush();

            cachedResponse = outputStream.toByteArray();
            memcacheService.put(cacheKey, cachedResponse, Expiration.byDeltaSeconds(3600));
        }

        httpResponse.getOutputStream().write(cachedResponse);
    }

    @Override
    public void destroy() {
        // No cleanup needed.
    }
}