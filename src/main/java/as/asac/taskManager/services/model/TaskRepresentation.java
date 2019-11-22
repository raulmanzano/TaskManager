package as.asac.taskManager.services.model;

/**
 * <p>TaskRepresentation class.</p>
 *
 */
public class TaskRepresentation {

    private String id;
    private String name;

    /**
     * <p>Constructor for TaskRepresentation.</p>
     *
     * @param id a {@link java.lang.String} object.
     * @param name a {@link java.lang.String} object.
     */
    public TaskRepresentation(String id, String name) {
        this.id = id;
        this.name = name;
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

}
