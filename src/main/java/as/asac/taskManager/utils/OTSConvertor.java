package as.asac.taskManager.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * <p>OTSConvertor class.</p>
 *
 */
@FacesConverter("es.uniovi.innova.OTSConvertor")
public class OTSConvertor implements Converter {
    /** {@inheritDoc} */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {        
        return value;
    }
    /** {@inheritDoc} */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {        
        return value.toString();
    }
}
