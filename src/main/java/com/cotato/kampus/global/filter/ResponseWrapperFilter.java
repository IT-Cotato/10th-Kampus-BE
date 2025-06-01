package com.cotato.kampus.global.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.cotato.kampus.global.wrapper.CustomHttpResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ResponseWrapperFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		if (response instanceof HttpServletResponse) {
			CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper((HttpServletResponse)response);
			chain.doFilter(request, responseWrapper);

			// 응답 데이터가 null이거나 비어있지 않은 경우에만 클라이언트로 출력
			byte[] responseData = responseWrapper.getResponseData();
			if (responseData != null && responseData.length > 0) {
				response.getOutputStream().write(responseData);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}