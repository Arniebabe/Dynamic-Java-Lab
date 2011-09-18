/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dyn.formatters;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acormier
 */
public class BasicFormatterTemplateIWant2 {
    
    public static final String DOUBLE_MESSAGE_TEMPLATE = "DoubleMessageTemplate: {0} unit{1}";
    public static final MethodType mtype = MethodType.methodType(Double.class);
    public MethodHandle mh;
    
    public BasicFormatterTemplateIWant2(MethodHandle _mh){
        mh=_mh;
    }
    
    public StringBuilder formatDouble(StringBuilder sb, Object o) {
        StringBuilder result = sb;
        MethodHandle mh2 = mh.bindTo(o);
        assert mtype.equals(mh2.type()); 
        try {
            Double d = (Double)mh2.invoke();
            if (d != null) {
                result = result.append(MessageFormat.format(DOUBLE_MESSAGE_TEMPLATE, d, (d > 1 ? "s" : "")));
            }
        } catch (Throwable ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
