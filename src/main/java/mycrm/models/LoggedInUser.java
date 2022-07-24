package mycrm.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoggedInUser extends User {

	private static final long serialVersionUID = -749477771982740983L;

	private Long id;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private Broker broker;

	public LoggedInUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			Long id, String firstName, String lastName, String emailAddress) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

		this.emailAddress = emailAddress;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
