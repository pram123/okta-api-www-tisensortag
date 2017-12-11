package com.okta.poc.iot.tisensor.apidynamodb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableOAuth2Sso
public class SpringSecurityWebAppConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenProvider tokenProvider;

    @Value("#{ @environment['okta.moreSecure.authorities'] }")
    String[] moreSecureAuthorities;

   /* @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails context) {
       // return new OAuth2RestTemplate(resource, context);
        return new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(context));
    }
    */

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {


        http
                .cors().and()
                .csrf().disable();
                /*.authorizeRequests()
                .antMatchers("/sapi").hasAnyAuthority(moreSecureAuthorities)
                .antMatchers("/anon/**").permitAll()
                .antMatchers("/profile/image").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/secure/").permitAll()
                .antMatchers("/secure").hasAnyAuthority(moreSecureAuthorities)
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JWTFilter(tokenProvider), BasicAuthenticationFilter.class);
                */
    }

    /*
    @Bean
    public AuthoritiesExtractor authoritiesExtractor(org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails template) {
        return map -> {
            List<String> groups = (List<String>) map.get("groups");
            return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", groups));
        };
    }*/


}