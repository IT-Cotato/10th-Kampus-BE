package com.cotato.kampus.global.intercepter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.cotato.kampus.global.wrapper.CustomHttpRequestWrapper;
import com.cotato.kampus.global.wrapper.CustomHttpResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String requestId = UUID.randomUUID().toString();
		request.setAttribute("X-Request-ID", requestId);
		response.addHeader("X-Request-ID", requestId);

		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);

		StringBuilder logMessage = new StringBuilder();
		logMessage.append("\n========== REQUEST START ==========\n");
		logMessage.append(String.format("Method: [%s] ID: [%s] URL: [%s]\n",
			request.getMethod(), requestId, request.getRequestURI()));

		// PathVariable 추가
		Map<String, String> pathVariables = getPathVariables(request, handler);
		if (!pathVariables.isEmpty()) {
			logMessage.append(String.format("PathVariables: %s\n", pathVariables));
		}

		// Request Parameters 추가
		Map<String, String> params = getRequestParams(request);
		if (!params.isEmpty()) {
			logMessage.append(String.format("Parameters: %s\n", params));
		}

		// Request Body 추가
		String requestBody = getRequestBody(request);
		if (!requestBody.isEmpty()) {
			logMessage.append(String.format("Body: \n%s\n", requestBody));
		}

		log.info(logMessage.toString());

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws JsonProcessingException {

		String requestId = (String)request.getAttribute("X-Request-ID");
		long startTime = (Long)request.getAttribute("startTime");
		long duration = System.currentTimeMillis() - startTime;

		StringBuilder logMessage = new StringBuilder();
		logMessage.append(String.format("\nStatus: [%s] ID: [%s] Duration: [%sms] URL: [%s]\n",
			response.getStatus(), requestId, duration, request.getRequestURI()));

		// Response Body 추가 (크기 제한)
		if (response instanceof CustomHttpResponseWrapper responseWrapper) {
			byte[] responseData = responseWrapper.getResponseData();
			if (responseData != null && responseData.length > 0) {
				if (responseData.length > 512) {
					logMessage.append(String.format("Body: [Too Large - %d bytes]\n", responseData.length));
				} else {
					String responseBody = new String(responseData);

					try {
						ObjectMapper mapper = new ObjectMapper();
						Object json = mapper.readValue(responseBody, Object.class);
						String prettyBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
						logMessage.append(String.format("Body: \n%s\n", prettyBody));
					} catch (JsonProcessingException e) {
						logMessage.append(String.format("Body: \n%s\n", responseBody));
					}
				}
			} else {
				logMessage.append("Body: [Empty]\n");
			}
		}

		logMessage.append("========== RESPONSE END ==========\n");

		log.info(logMessage.toString());
	}

	private Map<String, String> getPathVariables(HttpServletRequest request, Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return new HashMap<>();
		}

		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest currentRequest = attributes.getRequest();
			@SuppressWarnings("unchecked")
			Map<String, String> pathVariables = (Map<String, String>)currentRequest.getAttribute(
				HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

			return pathVariables != null ? pathVariables : new HashMap<>();
		}

		return new HashMap<>();
	}

	private String getRequestBody(HttpServletRequest request) {
		if (request instanceof CustomHttpRequestWrapper) {
			CustomHttpRequestWrapper requestWrapper = (CustomHttpRequestWrapper)request;
			String requestBody = new String(requestWrapper.getRequestBody());
			return requestBody.isEmpty() ? "" : requestBody;
		}
		return "";
	}

	private Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			paramMap.put(paramName, request.getParameter(paramName));
		}

		return paramMap;
	}
}