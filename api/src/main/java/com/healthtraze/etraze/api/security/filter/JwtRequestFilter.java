package com.healthtraze.etraze.api.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.healthtraze.etraze.api.TenantContext;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.security.model.UserSession;
import com.healthtraze.etraze.api.security.repository.UserSessionRepository;
import com.healthtraze.etraze.api.security.service.UserService;
import com.healthtraze.etraze.api.security.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	
	private final UserService myUserDetailsService;
	
	private final UserSessionRepository userSessionRepository;
	
	private final JwtUtil jwtUtil;
	
	@Autowired
	public JwtRequestFilter(UserService myUserDetailsService,UserSessionRepository userSessionRepository,JwtUtil jwtUtil) {
		this.myUserDetailsService = myUserDetailsService;
		this.userSessionRepository=userSessionRepository;
		this.jwtUtil=jwtUtil;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader(Constants.AUTHORIZATION);

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith(Constants.BEARER)) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUserName(jwt);
			} catch (ExpiredJwtException e) {
				response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
				logger.error(Constants.JWTREQUESTFILTER, e);
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
			if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				Map<?, ?> u = jwtUtil.extractAllClaims(jwt);
				String sessionId = (String) u.get(Constants.SESSIONID);
				UserSession s = userSessionRepository.findBySessionId(sessionId);

				if (s != null) {

				}

			}
		}
		
		HttpServletRequest req = request;
        String tenantName = req.getHeader("X-TenantID");
        TenantContext.setCurrentTenant(tenantName);
		
		
		try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.setCurrentTenant("");
        }
	}

}
