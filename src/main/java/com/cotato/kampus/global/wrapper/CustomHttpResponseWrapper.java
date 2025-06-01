package com.cotato.kampus.global.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CustomHttpResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private final PrintWriter writer = new PrintWriter(outputStream);

	public CustomHttpResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return new ServletOutputStream() {
			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setWriteListener(WriteListener listener) {

			}

			@Override
			public void write(int b) {
				outputStream.write(b);
			}
		};
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	public byte[] getResponseData() {
		writer.flush();
		return outputStream.toByteArray();
	}
}