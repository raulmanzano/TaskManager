package as.asac.taskManager.services.model;

import org.activiti.engine.task.Attachment;

/**
 * <p>AttachRepresentation class.</p>
 *
 */
public class AttachRepresentation {    	
	private String id;
	private String nombre;
    private String tipo;
    private String descripcion;
    
	
	/**
	 * <p>Constructor for AttachRepresentation.</p>
	 *
	 * @param attach a {@link org.activiti.engine.task.Attachment} object.
	 */
	public AttachRepresentation(Attachment attach) {
		this.id=attach.getId();
		this.nombre = attach.getName();
		this.tipo = attach.getType();
		this.descripcion = attach.getDescription();
	}
	/**
	 * <p>Getter for the field <code>nombre</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * <p>Setter for the field <code>nombre</code>.</p>
	 *
	 * @param nombre a {@link java.lang.String} object.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * <p>Getter for the field <code>tipo</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * <p>Setter for the field <code>tipo</code>.</p>
	 *
	 * @param tipo a {@link java.lang.String} object.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * <p>Getter for the field <code>descripcion</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * <p>Setter for the field <code>descripcion</code>.</p>
	 *
	 * @param descripcion a {@link java.lang.String} object.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
	
	
}
