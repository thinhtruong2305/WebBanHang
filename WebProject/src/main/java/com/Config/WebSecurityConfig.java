package com.Config;

import com.Database.service.impl.UserDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService(){
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider(){
		DaoAuthenticationProvider authenProvider = new DaoAuthenticationProvider();
		authenProvider.setPasswordEncoder(passwordEncoder());
		authenProvider.setUserDetailsService(userDetailsService());
		return authenProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/Home","/Authentication/**","/Client/**","/src/main/resources/static/Client/img/product/images/**").permitAll()
				.antMatchers("/Admin/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().accessDeniedPage("/Authentication/error403")
				.and()
			.formLogin()
				.loginProcessingUrl("/j_spring_security_check")
				.loginPage("/Authentication/Login")
				.permitAll()
			.defaultSuccessUrl("/Home")
				.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/Home")
			.deleteCookies("remember-me")
				.permitAll()
				.and()
			.rememberMe().tokenValiditySeconds(86400);
	}
}
