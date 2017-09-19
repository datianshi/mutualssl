package com.example.mutualssl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@SpringBootApplication
@EnableWebSecurity
public class MutualsslServerApplication extends WebSecurityConfigurerAdapter {

	protected final static Log logger = LogFactory.getLog(MutualsslServerApplication.class);

	@Bean
	public UserDetailsService userDetailsService()
	{
		return username -> User.withUsername(username).password("NOT-USED").roles("USER").build();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(new LogFilter(), X509AuthenticationFilter.class)
				.x509()
				.subjectPrincipalRegex("CN=(.*?),")
				.and()
				.authorizeRequests()
				.antMatchers("/nomutual").permitAll()
				.anyRequest().authenticated();
	}

	static {
		System.setProperty("javax.net.debug", "ssl");
	}

	static class LogFilter implements Filter {

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {

		}

		@Override
		public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
			HttpServletRequest request =  (HttpServletRequest)servletRequest;
			logger.info("X-Forward-Client-Cert: " + request.getHeader("X-Forwarded-Client-Cert"));
			filterChain.doFilter(servletRequest, servletResponse);
		}

		@Override
		public void destroy() {

		}
	}


	public static void main(String[] args) {
		SpringApplication.run(MutualsslServerApplication.class, args);
	}
}



