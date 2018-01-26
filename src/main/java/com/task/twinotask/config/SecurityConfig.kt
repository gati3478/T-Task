package com.task.twinotask.config

import com.task.twinotask.service.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
	
	private var clientService: ClientService? = null

	@Autowired
	fun setClientService(clientService: ClientService) {
		this.clientService = clientService
	}

	override fun configure(http: HttpSecurity) {
		http
			.authorizeRequests()
				.antMatchers(
					"/registration",
					"/js/**",
					"/css/**",
					"/img/**",
					"/webjars/**",
					"/test-save",
					"/find-by-id",
					"/find-by-email",
					"/find-all",
					"/h2_console/**"
				)
				.permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/index?success=true", true)
				.permitAll()
			.and()
				.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.permitAll()

		// For h2
		http.csrf().disable()
		http.headers().frameOptions().disable()
	}

	@Bean
	fun passwordEncoder() = BCryptPasswordEncoder()

	@Bean
	fun authenticationProvider(): DaoAuthenticationProvider {
		val auth = DaoAuthenticationProvider()
		auth.setUserDetailsService(clientService)
		auth.setPasswordEncoder(passwordEncoder())
		return auth
	}

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.authenticationProvider(authenticationProvider())
	}

}
