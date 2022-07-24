package mycrm.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

/*	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;*/

	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth)
	 * throws Exception {
	 * 
	 * 
	 * auth.jdbcAuthentication() 
	 * .usersByUsernameQuery(usersQuery)
	 * .authoritiesByUsernameQuery(rolesQuery) 
	 * .dataSource(dataSource)
	 * .passwordEncoder(bCryptPasswordEncoder); 
	 * }
	 */

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {		
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/")
		.permitAll()
		.antMatchers("/login").permitAll().antMatchers("/admin/**")
				.hasAnyAuthority("ADMIN", "SUPERADMIN", "BROKER", "EXTERNAL_BROKER", "LEADS").anyRequest()
				.authenticated()
				.and()
				.csrf()
				.disable()
				.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/admin/index")
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
				.exceptionHandling().accessDeniedPage("/access-denied");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/fragments/**", "/webjars/**", "/static/**", "/css/**", "/js/**",
				"/images/**");
	}

}
