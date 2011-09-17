/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dyn.formatters;

import java.text.MessageFormat;

/**
 *
 * @author acormier
 */
public class BasicFormatterTemplateIWant {
    
    public static final String DOUBLE_MESSAGE_TEMPLATE = "DoubleMessageTemplate: {0} unit{1}";
    
    public StringBuilder formatDouble(StringBuilder d, DataObjectTemplate req) {
        StringBuilder result = d;
        if (req.getDoubleProperty() != null) {
            result = result.append(MessageFormat.format(DOUBLE_MESSAGE_TEMPLATE, req.getDoubleProperty(), (req.getDoubleProperty() > 1 ? "s" : "")));
        }
        return result;
    }
}
