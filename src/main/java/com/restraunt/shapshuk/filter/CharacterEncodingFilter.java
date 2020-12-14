package com.restraunt.shapshuk.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = "/*", filterName = "ce_filter")
public class CharacterEncodingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(CharacterEncodingFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String characterEncoding = request.getCharacterEncoding();
        String utf8 = StandardCharsets.UTF_8.name();
        if (!utf8.equalsIgnoreCase(characterEncoding)) {
            request.setCharacterEncoding(utf8);
            response.setCharacterEncoding(utf8);
        }
        response.setContentType("text/html; charset=UTF-8");

        LOGGER.info("Character encoding filter done");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
