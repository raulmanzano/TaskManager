package as.asac.taskManager.services;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import as.asac.taskManager.beans.FormViewScopedBean;
import as.asac.taskManager.services.model.AttachRepresentation;
import as.asac.taskManager.services.model.FieldRepresentation;

/**
 * <p>ActivitiService class.</p>
 *
 */
@Service("activitiService")
@Transactional
public class ActivitiService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final Logger logger = LoggerFactory.getLogger(ActivitiService.class);
	

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private HistoryService historyService;
    
    @Value("#{${avaibleForms}}")
    Map<String,String> avaibleForms;
    
    @Value("#{'${allowedProcesses}'.split(',')}")
	List<String> allowedProcesses;	
	
	@PostConstruct
	    private void init() {	
	    }

	/**
	 * <p>process.</p>
	 *
	 * @param user a {@link java.lang.String} object.
	 * @param processDefinitionId a {@link java.lang.String} object.
	 * @param processInstanceId a {@link java.lang.String} object.
	 * @param taskId a {@link java.lang.String} object.
	 * @param isStartTask a boolean.
	 * @param variables a {@link java.util.Map} object.
	 * @param attachments a {@link java.util.List} object.
	 */
	public void process(String user,String processDefinitionId,String processInstanceId,String taskId,boolean isStartTask,Map<String, Object> variables,List<UploadedFile> attachments)    
    {    	    	
    	try {    				
			for (String variable :variables.keySet()) {				
				logger.debug(variable + " - "+ variables.get(variable));
			}     	
			if (isStartTask)
			{
				try {
					  identityService.setAuthenticatedUserId(user);
					  ProcessInstance instanceX= null;	  
					  if (variables.size()==0)
						  {instanceX = runtimeService.startProcessInstanceById(processDefinitionId);}
					  else 
						  {instanceX = runtimeService.startProcessInstanceById(processDefinitionId,variables);}
					  for (UploadedFile attach :attachments)
						{    			    			
							try {
								taskService.createAttachment(attach.getContentType(),null,instanceX.getId(), attach.getFileName(),attach.getFileName(), attach.getInputstream());
							} catch (Exception e) {
								logger.error("No se ha podido subir el documento");
								e.printStackTrace();
							}    			
						}			
					} finally {
					  identityService.setAuthenticatedUserId(null);
					}    		
			}
			else{
				for (UploadedFile attach :attachments)
				{
					try{
					taskService.createAttachment(attach.getContentType(),taskId,processInstanceId, attach.getFileName(),attach.getFileName(), attach.getInputstream());
				} catch (Exception e) {
					logger.error("No se ha podido subir el documento");
					e.printStackTrace();
				}
				}			

				
				Task task = taskService.createTaskQuery().taskId(taskId).singleResult();				

				
				//DESHABILITADOsi el asignado no es igual al usuario actual, se la asignamos
//				if (!(task.getAssignee().equalsIgnoreCase(bean.getUser())))
//				{
//					logger.info("Cambiando el owner");
//					task.setOwner(bean.getUser());
//				}					
				
				if (variables.size()==0)
					taskService.complete(taskId);				
				else taskService.complete(taskId, variables);
					
			}
		} catch (Exception e) {
			logger.error("Problemas procesando el formulario");			
		}    	
    }

    
    
  /**
   * <p>getTasks.</p>
   *
   * @param assignee a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   */
  public List<Task> getTasks(String assignee) {    	
	  return taskService.createTaskQuery().taskCandidateOrAssigned(assignee).includeProcessVariables().list();
}
    
  /**
   * <p>getTasks.</p>
   *
   * @param assignee a {@link java.lang.String} object.
   * @param processId a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   */
  public List<Task> getTasks(String assignee, String processId) {		
	  return taskService.createTaskQuery().taskCandidateOrAssigned(assignee).processInstanceId(processId).list();		
	}
  
    
	/**
	 * <p>getFileldsFromStartForm.</p>
	 *
	 * @param processDefinitionId a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<FieldRepresentation> getFileldsFromStartForm(
			String processDefinitionId) {
    	List<FormProperty> fields=null;
    	fields = formService.getStartFormData(processDefinitionId).getFormProperties();    	
    	logger.debug("Obtenidos "+fields.size() +" como campos del formulario de inicio ");    	
    	List<FieldRepresentation> dtos = new ArrayList<FieldRepresentation>();    	
        for (FormProperty field : fields) {        	
          dtos.add(new FieldRepresentation(field));
        }        
        return dtos;
    	
	}

	/**
	 * <p>getFileldsfromTask.</p>
	 *
	 * @param taskId a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<FieldRepresentation> getFileldsfromTask(String taskId) {
    	List<FormProperty> fields=null;
		fields = formService.getTaskFormData(taskId).getFormProperties();
		List<FieldRepresentation> dtos = new ArrayList<FieldRepresentation>();    	
        for (FormProperty field : fields) {        	
          dtos.add(new FieldRepresentation(field));
        }        
        return dtos;
	}
	
	/**
	 * <p>getProcessAttachs.</p>
	 *
	 * @param processDefinitionId a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<AttachRepresentation> getProcessAttachs(String processDefinitionId) {
		List<Attachment> attachsLocal = taskService.getProcessInstanceAttachments(processDefinitionId);		
		List<AttachRepresentation> dtos = new ArrayList<AttachRepresentation>();    	
        for (Attachment attach : attachsLocal) {        	
          dtos.add(new AttachRepresentation(attach));
        }        
        return dtos;
	}    

	/**
	 * <p>getTaskAttachs.</p>
	 *
	 * @param taskId a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<AttachRepresentation> getTaskAttachs(String taskId) {
		List<Attachment> attachsLocal = taskService.getTaskAttachments(taskId);		
		List<AttachRepresentation> dtos = new ArrayList<AttachRepresentation>();    	
        for (Attachment attach : attachsLocal) {        	
          dtos.add(new AttachRepresentation(attach));
        }        
        return dtos;
	}    
        
    
    

	/**
	 * <p>getProcess.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<ProcessDefinition> getProcess(String username) {
		List<ProcessDefinition> dtos = new ArrayList<ProcessDefinition>();    	
    	List<ProcessDefinition> ppp =repositoryService.createProcessDefinitionQuery().active().latestVersion().list();
    	for (ProcessDefinition p : ppp) {
    		if (allowedProcesses.contains(p.getKey())|| allowedProcesses.contains(p.getId()))    		
            dtos.add(p);
        }
        return dtos;
	}

	

	/**
	 * <p>getTargetFormFromFormKey.</p>
	 *
	 * @param formKey a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetFormFromFormKey(String formKey) {		
		return (this.avaibleForms.get(formKey)!=null)?avaibleForms.get(formKey):"form.xhtml";		
	}

	/**
	 * <p>getStartFormKey.</p>
	 *
	 * @param processId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getStartFormKey(String processId) {
		return formService.getStartFormKey(processId);
	}

	/**
	 * <p>getIniciatedProcessInstances.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<ProcessInstance> getIniciatedProcessInstances(String username) {
		try{
			identityService.setAuthenticatedUserId(username);		
		return runtimeService.createProcessInstanceQuery().variableValueEquals("initiator", username).includeProcessVariables().list();
			}finally{identityService.setAuthenticatedUserId(null);}
		}
	
	/**
	 * <p>desistir.</p>
	 *
	 * @param processInstance a {@link org.activiti.engine.runtime.ProcessInstance} object.
	 */
	public void desistir(ProcessInstance processInstance) {
//		logger.info("Intentando cancelar "+processInstance.getId());
//		
//		
//		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
//		for(Execution execution : executions){
//		  try {			  			
//			  logger.info("ProcessID ->" +processInstance.getId()+ " Executionid->"+execution.getId() + "SuperExecution->"+execution.getSuperExecutionId());
//			  
//		} catch (Exception e) {
//			logger.info("silenciada");
//		}		  
//		}
//		
//
//		logger.info("Padres");
//		for (ProcessInstance ppp : runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstance.getId()).list())
//		{
//			logger.info("ProcessID ->" +ppp.getId());
//			List<Execution> executionsa = runtimeService.createExecutionQuery().processInstanceId(ppp.getId()).list();
//			for(Execution execution : executionsa){
//			  try {			  			
//				  logger.info("ProcessID ->" +ppp.getId()+ " Executionid->"+execution.getId()+ "SuperExecution->"+execution.getSuperExecutionId());			
//			} catch (Exception e) {
//				logger.info("silenciada");
//			}
//			}
//		}
//		
//			logger.info("Hijos");
//			for (ProcessInstance ppp2 : runtimeService.createProcessInstanceQuery().subProcessInstanceId(processInstance.getId()).list())
//			{
//				logger.info("ProcessID ->" +ppp2.getId());
//				List<Execution> executions2 = runtimeService.createExecutionQuery().processInstanceId(ppp2.getId()).list();
//				for(Execution execution : executions2){
//				  try {			  			
//					  logger.info("ProcessID ->" +ppp2.getId()+ " Executionid->"+execution.getId()+ "SuperExecution->"+execution.getSuperExecutionId());			
//				} catch (Exception e) {
//					logger.info("silenciada");
//				}
//				}
//			}
//			
//			
//			//repositoryService.createProcessDefinitionQuery().messageEventSubscription(arg0)
//			
//			logger.info("porId");
//			for(Execution execution : runtimeService.createExecutionQuery().signalEventSubscription("desistir").list()){
//				  try {			  			
//					  logger.info("ProcessID ->" +execution.getProcessInstanceId()+ " Executionid->"+execution.getId()+ "SuperExecution->"+execution.getSuperExecutionId());
//				} catch (Exception e) {
//					logger.info("silenciada");
//				}		  
//				}
//			logger.info("pornombre");
//			for(Execution execution : runtimeService.createExecutionQuery().signalEventSubscriptionName("desistir").list()){
//				  try {			  			
//					  logger.info("ProcessID ->" +execution.getProcessInstanceId()+ " Executionid->"+execution.getId()+ "SuperExecution->"+execution.getSuperExecutionId());
//				} catch (Exception e) {
//					logger.info("silenciada");
//				}		  
//				}	
			
		logger.info("Desistiendo");
		for(Execution execution : runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list()){
		  try {			  			
		
			  if (!processInstance.getId().equalsIgnoreCase(execution.getId()))
			  		runtimeService.signalEventReceived("desistir",execution.getId());
			  
		} catch (Exception e) {
			logger.error("error",e);
		}		  
		}
		//runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), "DESISTIDO");
	}

	/**
	 * <p>getProcessDefinitionName.</p>
	 *
	 * @param task a {@link org.activiti.engine.task.Task} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getProcessDefinitionName(Task task) {
		return repositoryService.getProcessDefinition(task.getProcessDefinitionId()).getName();
	}

	/**
	 * <p>getAttachmentContent.</p>
	 *
	 * @param elemento a {@link as.asac.taskManager.services.model.AttachRepresentation} object.
	 * @return a {@link java.io.InputStream} object.
	 */
	public InputStream getAttachmentContent(AttachRepresentation elemento) {	
		return taskService.getAttachmentContent(elemento.getId());
	}

	/**
	 * <p>getInvolvedHistoricProcessInstances.</p>
	 *
	 * @param username a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<HistoricProcessInstance> getInvolvedHistoricProcessInstances(String username) {			
		List<HistoricProcessInstance> salida = new ArrayList<HistoricProcessInstance>();
		for (Group grupo :identityService.createGroupQuery().groupMember(username).list())
		{			
			if (grupo.getId().startsWith("OT_"))
			{
				for (String key : allowedProcesses)
				{			
				salida.addAll(historyService.createHistoricProcessInstanceQuery().processDefinitionKey(key).finished().includeProcessVariables().list());
				}
			}
			
		}
		for (String key : allowedProcesses)
				{			
					salida.addAll(historyService.createHistoricProcessInstanceQuery().involvedUser(username).processDefinitionKey(key).finished().includeProcessVariables().list());
				}	 		
		return salida;
	}

	
	
	
	/**
	 * <p>avanceProcess.</p>
	 *
	 * @param processId a {@link java.lang.String} object.
	 */
	public void avanceProcess(String processId)
	{

	 List<Task> taskListe = taskService.createTaskQuery().processInstanceId(processId).list();
	 for(Task tarea : taskListe)
	 {
		 try {
		 taskService.complete(tarea.getId());
		logger.info("Avanzando tarea "+tarea.getId()+"en proceso "+processId);
		} catch (Exception e) {
			logger.error("Error avanzando tarea "+tarea.getId()+"en proceso "+processId,e);			
		}
	 }			
		
	}

	/**
	 * <p>getTaskName.</p>
	 *
	 * @param taskId a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaskName(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).singleResult().getName();		
	}    
   }
    
    

