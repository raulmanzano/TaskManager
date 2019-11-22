package as.asac.taskManager.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import as.asac.taskManager.services.ActivitiService;
import as.asac.taskManager.services.model.AttachRepresentation;
import as.asac.taskManager.services.model.FieldRepresentation;
import as.asac.taskManager.utils.JsfUtil;




@ManagedBean
/**
 * <p>FormViewScopedBean class.</p>
 *
 */
@ViewScoped
public class FormViewScopedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FormViewScopedBean.class);
	
	
	@ManagedProperty(value = "#{userSessionBean}") 
	UserSessionBean userSessionBean;

	
	Map<String,Object> variables;
	List<FieldRepresentation> fields;
	
	List<AttachRepresentation> processAttachs;
	List<AttachRepresentation> taskAttachs;
	
	ArrayList<UploadedFile> attachments = new ArrayList<UploadedFile>();
	
	
	
	@Autowired
	private ActivitiService activitiService;
	
	//necesario para el autowired de JSF a Srping
	  @PostConstruct
	    private void init() {		  
		  
		  logger.debug("PostConstruct"+this.toString());		  		  
	    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        ServletContext servletContext = (ServletContext) externalContext.getContext();
			WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).
	                                   getAutowireCapableBeanFactory().
	                                   autowireBean(this);
	    }

	  
	  /**
	   * <p>handleFileUpload.</p>
	   *
	   * @param event a {@link org.primefaces.event.FileUploadEvent} object.
	   */
	  public void handleFileUpload(FileUploadEvent event) {
		  	attachments.add(event.getFile());		  	
		  	logger.debug(this.toString());
		  	logger.debug("uploaded");
	        JsfUtil.addSuccessMessage(event.getFile().getFileName() + " anexado.");		            	
	    }
	
	  
	/**
	 * <p>Getter for the field <code>variables</code>.</p>
	 *
	 * @return a java$util$Map object.
	 */
	public java.util.Map<String, Object> getVariables() {
		return variables;
	}
	/**
	 * <p>Setter for the field <code>variables</code>.</p>
	 *
	 * @param variables a java$util$Map object.
	 */
	public void setVariables(java.util.Map<String, Object> variables) {
		this.variables = variables;
	}

	
	/**
	 * <p>Getter for the field <code>fields</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<FieldRepresentation> getFields() {
		this.fields = new ArrayList<FieldRepresentation>();			
		logger.debug(userSessionBean.toString());
		
if (
		(userSessionBean.getProcessDefinitionId()!=null
		&& userSessionBean.isStartTask())
		||
		(userSessionBean.getTaskId()!=null
		&& !userSessionBean.isStartTask()))		
{		
		if (userSessionBean.isStartTask())
			{
			this.fields = activitiService.getFileldsFromStartForm(userSessionBean.getProcessDefinitionId());}
		else 
			{this.fields = activitiService.getFileldsfromTask(userSessionBean.getTaskId());}
		this.variables = new HashMap<String,Object>();
		for (FieldRepresentation campo : this.fields) {
			variables.put(campo.getId(),campo.getValue());				
		}
}		
		
		return this.fields;
	}
	/**
	 * <p>Setter for the field <code>fields</code>.</p>
	 *
	 * @param fields a {@link java.util.List} object.
	 */
	public void setFields(List<FieldRepresentation> fields) {
		this.fields = fields;
	}

	/**
	 * <p>Getter for the field <code>attachments</code>.</p>
	 *
	 * @return a {@link java.util.ArrayList} object.
	 */
	public ArrayList<UploadedFile> getAttachments() {
		return attachments;
	}


	/**
	 * <p>Setter for the field <code>attachments</code>.</p>
	 *
	 * @param attachments a {@link java.util.ArrayList} object.
	 */
	public void setAttachments(ArrayList<UploadedFile> attachments) {
		this.attachments = attachments;
	}

	
	/**
	 * <p>Getter for the field <code>processAttachs</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<AttachRepresentation> getProcessAttachs() {		
		this.processAttachs=activitiService.getProcessAttachs(userSessionBean.getProcessInstanceId());
		return processAttachs;
	}


	/**
	 * <p>Setter for the field <code>processAttachs</code>.</p>
	 *
	 * @param processAttachs a {@link java.util.List} object.
	 */
	public void setProcessAttachs(List<AttachRepresentation> processAttachs) {
		this.processAttachs = processAttachs;
	}


	/**
	 * <p>Getter for the field <code>taskAttachs</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<AttachRepresentation> getTaskAttachs() {
		this.taskAttachs=activitiService.getTaskAttachs(userSessionBean.getTaskId());
		return taskAttachs;
	}


	/**
	 * <p>Setter for the field <code>taskAttachs</code>.</p>
	 *
	 * @param taskAttachs a {@link java.util.List} object.
	 */
	public void setTaskAttachs(List<AttachRepresentation> taskAttachs) {
		this.taskAttachs = taskAttachs;
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


	/**
	 * <p>getUser.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getUser() {
    	return userSessionBean.getUsername();    	
    }


	/**
	 * <p>isStartTask.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isStartTask() {
		return this.userSessionBean.isStartTask(); 
		
	}


	/**
	 * <p>getProcessDefinitionId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProcessDefinitionId() {
		return this.userSessionBean.getProcessDefinitionId();
				}


	/**
	 * <p>getProcessInstanceId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProcessInstanceId() {
		return this.userSessionBean.getProcessInstanceId();
				}


	/**
	 * <p>getTaskId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaskId() {
		return this.userSessionBean.getTaskId();
				}

	/**
	 * <p>process.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String process() {	
		activitiService.process(userSessionBean.getUsername(),
				userSessionBean.getProcessDefinitionId(),
				userSessionBean.getProcessInstanceId(),
				userSessionBean.getTaskId(),
				userSessionBean.isStartTask(),
				variables,attachments);	
		return "index.xhtml";
	}
	
	/**
	 * <p>cancel.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String cancel() {	
		return "index.xhtml";
	}
	
	
	    /**
	     * <p>download.</p>
	     *
	     * @param elemento a {@link as.asac.taskManager.services.model.AttachRepresentation} object.
	     * @return a {@link org.primefaces.model.StreamedContent} object.
	     */
	    public StreamedContent download(AttachRepresentation elemento) {
	        InputStream stream = activitiService.getAttachmentContent(elemento);  
	        return new DefaultStreamedContent(stream, elemento.getTipo(), elemento.getNombre());
	    }
	    
	    /**
	     * <p>deleteAttach.</p>
	     *
	     * @param indice a int.
	     */
	    public void deleteAttach(int indice)
	    {	    	
	    	attachments.remove(indice);
	    	JsfUtil.addSuccessMessage("Eliminado");
	    }

	    
	    /**
	     * <p>getTaskName.</p>
	     *
	     * @return a {@link java.lang.String} object.
	     */
	    public String getTaskName()
	    {
	    	if (userSessionBean.getTaskId()==null)
	    	{
	    		return null;
	    	}else 	    		
	    	return activitiService.getTaskName(userSessionBean.getTaskId());
	    }
	    
	
	}
