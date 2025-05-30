package com.cotato.kampus.global.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.cotato.kampus.global.wrapper.CustomHttpRequestWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestWrapperFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(httpRequest);
			chain.doFilter(requestWrapper, response);  // CustomHttpRequestWrapper로 감싼 후 체인 진행
		} else {
			chain.doFilter(request, response);
		}
	}
}