package as.asac.taskManager.services.model;

import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;

//import org.activiti.bpmn.model.FormProperty;


/**
 * <p>FieldRepresentation class.</p>
 *
 */
public class FieldRepresentation {
    private String id=null;
    private String name=null;
    private String type=null;
    private String value=null;
    private boolean readable=false;
    private boolean writable=false;
    private boolean required=false;   
    private Map<String, String> values=null;
    
	/**
	 * <p>Constructor for FieldRepresentation.</p>
	 *
	 * @param field a {@link org.activiti.engine.form.FormProperty} object.
	 */
	public FieldRepresentation(FormProperty field) {
		this.id=field.getId();		
		this.type=field.getType().getName();
		this.name=field.getName();		
		if (this.id.contains("textarea"))
			{this.type="textarea";}
		
		this.value=field.getValue();				
		if (this.type.equalsIgnoreCase("enum"))
		{
			this.values = (Map<String, String>)field.getType().getInformation("values");
		}		
		this.readable=field.isReadable();
		this.writable=field.isWritable();
		this.required = field.isRequired();
		
	}
	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId() {
		return id;
	}
	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}
	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getType() {
		return type;
	}
	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * <p>isReadable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isReadable() {
		return readable;
	}
	/**
	 * <p>Setter for the field <code>readable</code>.</p>
	 *
	 * @param readable a boolean.
	 */
	public void setReadable(boolean readable) {
		this.readable = readable;
	}
	/**
	 * <p>isWritable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isWritable() {
		return writable;
	}
	/**
	 * <p>Setter for the field <code>writable</code>.</p>
	 *
	 * @param writable a boolean.
	 */
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	/**
	 * <p>isRequired.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * <p>Setter for the field <code>required</code>.</p>
	 *
	 * @param required a boolean.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * <p>Getter for the field <code>values</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, String> getValues() {
		return values;
	}
	/**
	 * <p>Setter for the field <code>values</code>.</p>
	 *
	 * @param values a {@link java.util.Map} object.
	 */
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
    

}
