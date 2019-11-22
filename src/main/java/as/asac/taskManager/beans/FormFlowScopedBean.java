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
import javax.faces.bean.SessionScoped;
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
 * <p>FormFlowScopedBean class.</p>
 *
 */
@SessionScoped
public class FormFlowScopedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FormFlowScopedBean.class);
	
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
	   * <p>reset.</p>
	   */
	  public void reset()
	  {		  
		  	this.variables = null;
		  	this.fields= null;			
		  	this.processAttachs= null;
		  	this.taskAttachs= null;		
		  	this.attachments = new ArrayList<UploadedFile>();
	  }
	  
	  
	  
	  /**
	   * <p>handleFileUpload.</p>
	   *
	   * @param event a {@link org.primefaces.event.FileUploadEvent} object.
	   */
	  public void handleFileUpload(FileUploadEvent event) {
		  	attachments.add(event.getFile());		  	
		  	//logger.debug(this.toString());
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
	 * <p>getFieldsStatic.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<FieldRepresentation> getFieldsStatic() {
		
		if (this.fields==null)
		{
			return getFields();
		}else 
		return this.fields;
	}
	
	
	/**
	 * <p>Getter for the field <code>fields</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<FieldRepresentation> getFields() {
		this.fields = new ArrayList<FieldRepresentation>();			
		//logger.debug(userSessionBean.toString());
		
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
	 * <p>cancel.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String cancel() {	
		reset();
		return "index.xhtml";
	}
	
	/**
	 * <p>processTwoStep1.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String processTwoStep1() {
		logger.info(this.toString());
		return "formTwoStepConfirmation2.xhtml";
	}
	
	/**
	 * <p>processTwoStep2.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String processTwoStep2() {		
		//procesa la primera tarea y avanza un paso mas.
		activitiService.process(userSessionBean.getUsername(),
				userSessionBean.getProcessDefinitionId(),
				userSessionBean.getProcessInstanceId(),
				userSessionBean.getTaskId(),
				userSessionBean.isStartTask(),
				variables,attachments);			
		activitiService.avanceProcess(getProcessInstanceId());		
		reset();
		return "index.xhtml";
	}

	/**
	 * <p>processTwoStep2Cancel.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String processTwoStep2Cancel() {	
		logger.info(this.toString());
		return "formTwoStepConfirmation.xhtml";
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
	    	return activitiService.getTaskName(userSessionBean.getTaskId());
	    }
	    

	    /**
	     * <p>toString.</p>
	     *
	     * @return a {@link java.lang.String} object.
	     */
	    public String toString()	    
	    {
	    	String salida = "";
	    	
	    	if (fields!=null)
	    	for (FieldRepresentation campo:  fields)
	    	{
	    		salida+="\n"+campo.getId();
	    	}	    	
	    	if (variables!=null)
	    	{
	    		salida+="\nnumero de variables "+variables.keySet().size();
	    	for (String variableNombre:  variables.keySet())
	    	{
	    		salida+="\n"+variableNombre+" "+variables.get(variableNombre);
	    	}
	    	}
	    	if (processAttachs!=null)
	    	for (AttachRepresentation attach:  processAttachs)
	    	{
	    		salida+="\n"+attach.getNombre();
	    	}
	    	if (taskAttachs!=null)
	    	for (AttachRepresentation attach:  taskAttachs)
	    	{
	    		salida+="\n"+attach.getNombre();
	    	}
	    	if (attachments!=null)
	    	for (UploadedFile attach:  attachments)
	    	{
	    		salida+="\n"+attach.getFileName();
	    	}
	    	
	    	return salida;
	    }
	    
	    
	    
	    
	}
