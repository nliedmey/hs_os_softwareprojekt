package de.swprojekt.speeddating.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/*
 * Konfiguration von Spring Security
 * Quelle: https://www.youtube.com/watch?v=IyzC1kkHZ-I
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;	//Nutzung in createDaoAuthenticationProvider

//Ueberreste aus Tutorial, wird wahrscheinlich nicht benoetigt
//	@Autowired	//Setter-based Injection
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(userDetailsService);
//		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());		//Art der Verschluesselung des Passworts wird festgelegt, 
//		auth.userDetailsService(userDetailsService).and().authenticationProvider(authenticationProvider);
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).and().authorizeRequests()
				.antMatchers("/VAADIN/**", "/PUSH/**", "/UIDL/**", "/login", "/signup", "/login/**", "/logout",
						"/vaadinServlet/**")
				.permitAll(); // diese Pfade sind unauthentifiziert erreichbar
				//.antMatchers("/ui", "/ui/**").fullyAuthenticated(); //diese Pfade muessen authentifiziert betreten werden
				//ueber @Secured("ROLE_ADMIN")-Annotation an View kann diese auf die besagte Rolle beschraenkt werden,
				//dies ist Sicherer als die Festlegung bestimmter gesicherter Bereiche (wie Zeile oben ueber AntMatchers), da bei diesen
				//bei Weiterleitungen ueber Vaadin-RouterLinks das Recht nicht erneut geprueft wird
																	  
	}

	@Bean // wird somit in Kontext geladen, damit Autowire an anderen Stellen moeglich
	public DaoAuthenticationProvider createDaoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);	//userDetailsService-Interface wurde autowired
		provider.setPasswordEncoder(passwordEncoder());	//Methode zur Passwortverschluesselung festlegen
		return provider;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {	//wird in RegisterUserServiceImpl benoetigt (via Autowire dort eingebunden)
		return new BCryptPasswordEncoder();
	}

}
