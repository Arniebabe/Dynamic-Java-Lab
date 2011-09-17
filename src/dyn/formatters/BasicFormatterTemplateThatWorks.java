/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dyn.formatters;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acormier
 */
public class BasicFormatterTemplateThatWorks {

    public static final String DOUBLE_MESSAGE_TEMPLATE = "DoubleMessageTemplate: {0} unit{1}";
    public static final MethodType desc = MethodType.methodType(Double.class);

    public StringBuilder formatDouble(StringBuilder d, Object req) {
        System.out.println("called");
        StringBuilder result = d;
        System.out.println("created Result");
        try {
            
            MethodHandle mh1 = MethodHandles.lookup().findVirtual(req.getClass(), "getDoubleProperty", desc);
            MethodHandle mh2 = mh1.bindTo(req);
            if (mh2.invoke() != null) {
                result = result.append(MessageFormat.format(DOUBLE_MESSAGE_TEMPLATE, (Double)mh2.invoke(), ((Double)mh2.invoke() > 1 ? "s" : "")));
            }
        } catch (Throwable ex) {
            Logger.getLogger(BasicFormatterTemplateThatWorks.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
