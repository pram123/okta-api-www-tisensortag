package com.okta.poc.iot.tisensor.apidynamodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.okta.poc.iot.tisensor.apidynamodb"})
@EntityScan(basePackages = {"com.okta.poc.iot.tisensor.apidynamodb"})  // scan JPA entitiessh
public class OktaAPIWWWTiSensorTag {

	private final String ACCESS_TOKEN_COOKIE_NAME = "access_token";

	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Autowired
	public OktaAPIWWWTiSensorTag(CustomAccessDeniedHandler customAccessDeniedHandler) {
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}

	@EnableGlobalMethodSecurity(prePostEnabled = true)
	protected static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return new OAuth2MethodSecurityExpressionHandler();
		}
	}

	@Bean
	protected ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
		return new ResourceServerConfigurerAdapter() {

			@Override
			public void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests()
						.antMatchers("/login.html", "/login", "/images/**", "/", "/swagger-ui/**","/assets/**").permitAll()
						.and()
						.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
			}

			@Override
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.tokenExtractor(new TokenExtractor() {

					@Override
					public Authentication extract(HttpServletRequest request) {
						String tokenValue = findCookie(ACCESS_TOKEN_COOKIE_NAME, request.getCookies());
						if (tokenValue == null) {
							return null;
						}

						return new PreAuthenticatedAuthenticationToken(tokenValue, "");
					}

					private String findCookie(String name, Cookie[] cookies) {
						if (name == null || cookies == null) {
							return null;
						}
						for (Cookie cookie : cookies) {
							if (name.equals(cookie.getName())) {
								return cookie.getValue();
							}
						}
						return null;
					}
				});
			}
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(OktaAPIWWWTiSensorTag.class, args);
	}
}
