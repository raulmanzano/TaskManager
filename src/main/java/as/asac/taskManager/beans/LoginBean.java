package as.asac.taskManager.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import as.asac.taskManager.utils.JsfUtil;

@ManagedBean
/**
 * <p>LoginBean class.</p>
 *
 */
@RequestScoped

public class LoginBean {

	@Autowired
	private AuthenticationManager authenticationManager;

	private String username;

	private String password;

	
	@ManagedProperty(value = "#{userSessionBean}") 
	UserSessionBean userSessionBean;
	
	//necesario para el autowired de JSF a Srping
	  @PostConstruct
	    private void init() {		  		 
	    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        ServletContext servletContext = (ServletContext) externalContext.getContext();
			WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).
	                                   getAutowireCapableBeanFactory().
	                                   autowireBean(this);			
	    }

	
	/**
	 * <p>Constructor for LoginBean.</p>
	 */
	public LoginBean() {
	}

	
	
	/**
	 * <p>login.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String login() {
	    try {
	        Authentication authentication = authenticationManager
	                .authenticate(new UsernamePasswordAuthenticationToken(
	                        this.username, this.password));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	    } catch (AuthenticationException ex) {
	        JsfUtil.addErrorMessage("Credenciales erróneas");	    		    	
	        return "login.xhtml";
	    }
	    userSessionBean.setUsername(this.username);
	    return "/secure/index.xhtml";
	}

//no funciona
//	public String logout() {
//	    return "/logout";
//	}

	/**
	 * <p>Getter for the field <code>authenticationManager</code>.</p>
	 *
	 * @return a {@link org.springframework.security.authentication.AuthenticationManager} object.
	 */
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	/**
	 * <p>Setter for the field <code>authenticationManager</code>.</p>
	 *
	 * @param authenticationManager a {@link org.springframework.security.authentication.AuthenticationManager} object.
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * <p>Getter for the field <code>username</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * <p>Setter for the field <code>username</code>.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * <p>Getter for the field <code>password</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * <p>Setter for the field <code>password</code>.</p>
	 *
	 * @param password a {@link java.lang.String} object.
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * <p>Getter for the field <code>userSessionBean</code>.</p>
	 *
	 * @return a {@link as.asac.taskManager.beans.UserSessionBean} object.
	 */
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}


	/**
	 * <p>Setter for the field <code>userSessionBean</code>.</p>
	 *
	 * @param userSessionBean a {@link as.asac.taskManager.beans.UserSessionBean} object.
	 */
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}
	
	
	
	
}
