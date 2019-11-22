package as.asac.taskManager.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import as.asac.taskManager.services.ActivitiService;
import as.asac.taskManager.utils.JsfUtil;


@ManagedBean
/**
 * <p>IndexViewBean class.</p>
 *
 */
@ViewScoped
public class IndexViewBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(IndexViewBean.class);
	
	@ManagedProperty(value = "#{userSessionBean}") 
	UserSessionBean userSessionBean;
	
	@Autowired
	private ActivitiService myActivitiService;
	
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
     * <p>getProcess.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProcessDefinition> getProcess() {
    	return myActivitiService.getProcess(userSessionBean.getUsername());
    }
	  
    
    /**
     * <p>getTasks.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Task> getTasks() {
    	return myActivitiService.getTasks(userSessionBean.getUsername());    	
    }

    /**
     * <p>tasksOfProcess.</p>
     *
     * @param processInstance a {@link org.activiti.engine.runtime.ProcessInstance} object.
     * @return a {@link java.util.List} object.
     */
    public List<Task> tasksOfProcess(ProcessInstance processInstance) {
    	return myActivitiService.getTasks(userSessionBean.getUsername(),processInstance.getId());    	
    }
    
    
    /**
     * <p>getProcessDefinitionName.</p>
     *
     * @param task a {@link org.activiti.engine.task.Task} object.
     * @return a {@link java.lang.String} object.
     */
    public String getProcessDefinitionName(Task task) {
    	return myActivitiService.getProcessDefinitionName(task);    	
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
	 * <p>Getter for the field <code>myActivitiService</code>.</p>
	 *
	 * @return a {@link as.asac.taskManager.services.ActivitiService} object.
	 */
	public ActivitiService getMyActivitiService() {
		return myActivitiService;
	}


	/**
	 * <p>Setter for the field <code>myActivitiService</code>.</p>
	 *
	 * @param myActivitiService a {@link as.asac.taskManager.services.ActivitiService} object.
	 */
	public void setMyActivitiService(ActivitiService myActivitiService) {
		this.myActivitiService = myActivitiService;
	}
    
	
	/**
	 * <p>getIniciatedProcessInstances.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ProcessInstance> getIniciatedProcessInstances() {
		return myActivitiService.getIniciatedProcessInstances(userSessionBean.getUsername());		
	};

	/**
	 * <p>getInvolvedHistoricProcessInstances.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<HistoricProcessInstance> getInvolvedHistoricProcessInstances() {
		//solo se van a pedir del proceso configurado
		return myActivitiService.getInvolvedHistoricProcessInstances(userSessionBean.getUsername());		
	};
		
	
	/**
	 * <p>start.</p>
	 *
	 * @param processDefinitionId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String start(String processDefinitionId){
		userSessionBean.reset();
		userSessionBean.setStartTask(true);
		userSessionBean.setFormKey(myActivitiService.getStartFormKey(processDefinitionId));
		userSessionBean.setProcessDefinitionId(processDefinitionId);		
	return myActivitiService.getTargetFormFromFormKey(myActivitiService.getStartFormKey(processDefinitionId))+"?faces-redirect=true";		
	}
    
	/**
	 * <p>complete.</p>
	 *
	 * @param tarea a {@link org.activiti.engine.task.Task} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String complete(Task tarea){
		userSessionBean.reset();		
		userSessionBean.setStartTask(false);
		userSessionBean.setFormKey(tarea.getFormKey());
		userSessionBean.setTaskId(tarea.getId());
		userSessionBean.setProcessDefinitionId(tarea.getProcessDefinitionId());								
		userSessionBean.setProcessInstanceId(tarea.getProcessInstanceId());								
		return myActivitiService.getTargetFormFromFormKey(tarea.getFormKey())+"?faces-redirect=true";
	}
	
	/**
	 * <p>desistir.</p>
	 *
	 * @param processInstance a {@link org.activiti.engine.runtime.ProcessInstance} object.
	 */
	public void desistir(ProcessInstance processInstance){
		logger.info("Desistiendo");				
		myActivitiService.desistir(processInstance);
		JsfUtil.addSuccessMessage("Desistido");		
		//return "index.xhtml?faces-redirect=true";
	}
	
	
    
    
	  
	}
