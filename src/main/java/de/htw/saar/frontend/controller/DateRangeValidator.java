package de.htw.saar.frontend.controller;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import org.apache.http.protocol.RequestContent;
import org.primefaces.context.RequestContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;

@FacesValidator(value = "dateRangeValidator")
public class DateRangeValidator implements Validator {
    @Override
    public void validate(FacesContext context,
                         UIComponent uiComponent, Object o) throws ValidatorException {
        UIInput startDateInput = (UIInput) uiComponent.getAttributes().get("startDateAttr");
        Date startDate = (Date) startDateInput.getValue();
        Date endDate = (Date) o;

        if (endDate.before(startDate)) {
            FacesMessage msg =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Vorsicht!",
                            "Start-Time kann nicht vor End-Time sein! ");
            throw new ValidatorException(msg);
        } else {
            RequestContext c = RequestContext.getCurrentInstance();
            c.execute("PF('dateRangeDlg').hide();");
            c.execute("location.reload();");
        }
    }
}
