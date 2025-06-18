package org.springframework.samples.petclinic.system;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple rate limiting interceptor for POC purposes. Limits requests per IP address with
 * a sliding window approach.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

	@Value("${rate-limit.max-requests:5}")
	private int maxRequests;

	@Value("${rate-limit.window-size-minutes:1}")
	private int windowSizeMinutes;

	@Value("${rate-limit.enabled:true}")
	private boolean rateLimitEnabled;

	// Simple in-memory storage (not production-ready)
	private final ConcurrentHashMap<String, RequestTracker> requestCounts = new ConcurrentHashMap<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// Skip if rate limiting is disabled
		if (!rateLimitEnabled) {
			return true;
		}

		String requestURI = request.getRequestURI();

		// Only apply rate limiting to /owners/find endpoint
		if (!"/owners/find".equals(requestURI)) {
			return true;
		}

		String clientIP = getClientIP(request);
		String key = clientIP + ":" + requestURI;

		RequestTracker tracker = requestCounts.compute(key, (k, v) -> {
			if (v == null) {
				return new RequestTracker();
			}
			return v;
		});

		if (isRateLimited(tracker)) {
			response.setStatus(429); // Too Many Requests
			response.setContentType("application/json");

			String jsonResponse = """
					{
						"error": "Rate limit exceeded. Try again later.",
						"maxRequests": %d,
						"windowSizeMinutes": %d
					}""".formatted(maxRequests, windowSizeMinutes);

			response.getWriter().write(jsonResponse);
			return false;
		}

		return true;
	}

	private boolean isRateLimited(RequestTracker tracker) {
		long currentTime = System.currentTimeMillis();
		long windowSizeMs = windowSizeMinutes * 60 * 1000L; // Convert minutes to
															// milliseconds

		// Reset counter if window has passed
		if (currentTime - tracker.windowStart.get() > windowSizeMs) {
			tracker.windowStart.set(currentTime);
			tracker.requestCount.set(1);
			return false;
		}

		// Check if we've exceeded the limit
		return tracker.requestCount.incrementAndGet() > maxRequests;
	}

	private String getClientIP(HttpServletRequest request) {
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
			return xForwardedFor.split(",")[0].trim();
		}

		String xRealIP = request.getHeader("X-Real-IP");
		return (xRealIP != null && !xRealIP.isEmpty()) ? xRealIP : request.getRemoteAddr();
	}

	private static class RequestTracker {

		final AtomicInteger requestCount = new AtomicInteger(0);

		final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

	}

}