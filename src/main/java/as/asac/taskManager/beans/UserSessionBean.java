package as.asac.taskManager.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@ManagedBean
/**
 * <p>UserSessionBean class.</p>
 *
 */
@SessionScoped
public class UserSessionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(UserSessionBean.class);
	
	String username;

	boolean startTask;
	String formKey;							
	String processDefinitionId;				
	String taskId;								
	String processInstanceId;				
	
	@PostConstruct
	    private void init() {
		 //this.username ="kermit";		  
		  reset();
	    }

	/**
	 * <p>reset.</p>
	 */
	public void reset()
	{
		startTask= false;
		formKey=null;							
		processDefinitionId=null;				
		taskId=null;								
		processInstanceId=null;						
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
	 * <p>isStartTask.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isStartTask() {
		return startTask;
	}

	/**
	 * <p>Setter for the field <code>startTask</code>.</p>
	 *
	 * @param startTask a boolean.
	 */
	public void setStartTask(boolean startTask) {
		this.startTask = startTask;
	}

	/**
	 * <p>Getter for the field <code>formKey</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFormKey() {
		return formKey;
	}

	/**
	 * <p>Setter for the field <code>formKey</code>.</p>
	 *
	 * @param formKey a {@link java.lang.String} object.
	 */
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	/**
	 * <p>Getter for the field <code>processDefinitionId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	/**
	 * <p>Setter for the field <code>processDefinitionId</code>.</p>
	 *
	 * @param processDefinitionId a {@link java.lang.String} object.
	 */
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	/**
	 * <p>Getter for the field <code>taskId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * <p>Setter for the field <code>taskId</code>.</p>
	 *
	 * @param taskId a {@link java.lang.String} object.
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * <p>Getter for the field <code>processInstanceId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	/**
	 * <p>Setter for the field <code>processInstanceId</code>.</p>
	 *
	 * @param processInstanceId a {@link java.lang.String} object.
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return " username->"+username
		+" startTask->"+startTask
		+" formKey->"+formKey							
		+" processDefinitionId->"+processDefinitionId				
		+" taskId->"+taskId								
		+" processInstanceId->"+processInstanceId;				

	}
	  
	
	
	
	
	  
	}
