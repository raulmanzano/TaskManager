package as.asac.taskManager.utils;

import java.net.URL;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;



/**
 * <p>JsfUtil class.</p>
 *
 */
public class JsfUtil {

	
	
	private static final Logger logger = LoggerFactory.getLogger(JsfUtil.class);
	
	
    /**
     * <p>getSelectItems.</p>
     *
     * @param entities a {@link java.util.List} object.
     * @param selectOne a boolean.
     * @return an array of {@link javax.faces.model.SelectItem} objects.
     */
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    /**
     * <p>isValidationFailed.</p>
     *
     * @return a boolean.
     */
    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    /**
     * <p>addErrorMessage.</p>
     *
     * @param ex a {@link java.lang.Exception} object.
     * @param defaultMsg a {@link java.lang.String} object.
     */
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    /**
     * <p>addErrorMessages.</p>
     *
     * @param messages a {@link java.util.List} object.
     */
    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    /**
     * <p>addErrorMessage.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        
    }

    /**
     * <p>addSuccessMessage.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    /**
     * <p>getRequestParameter.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    /**
     * <p>getObjectFromRequestParameter.</p>
     *
     * @param requestParameterName a {@link java.lang.String} object.
     * @param converter a {@link javax.faces.convert.Converter} object.
     * @param component a {@link javax.faces.component.UIComponent} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    
    
    /**
     * <p>getSavedUrl.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getSavedUrl() {
        HttpServletRequest request = ((HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest());

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(
                request, (HttpServletResponse) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getResponse());

        if (savedRequest != null) {
            try {
                URL url = new URL(savedRequest.getRedirectUrl());
                return url.getFile().substring(
                        request.getContextPath().length());
            } catch (Exception e) {
                logger.error(e.getMessage() + " Using default URL");
            }
        }
        return "admin/index.xhtml?faces-redirect=true"; // default page!
    }
    
}
