package dyn.formatters;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.invoke.anon.AnonymousClassLoader;
import sun.invoke.anon.ConstantPoolPatch;

/**
 * The possibilities offered by AnonymousClassLoader to path the ConstantPool look very much like good old Unix's fork()
 * system call : in principle, only what gets changed needs to be duplicated. I suspect a smart JVM implementation
 * could keep the same execution code and only duplicate Constant Pools.
 * @author acormier
 */
public class GeneratorThatWorks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ConstantPoolPatch patch = new ConstantPoolPatch(BasicFormatterTemplateThatWorks.class);
            HashMap<String, String> utf8Map = new HashMap<>();
            utf8Map.put("getDoubleProperty", "getValue");

            HashMap<String, Object> classMap = new HashMap<>();
            HashMap<Object, Object> valueMap = new HashMap<>();
            patch.putPatches(utf8Map, classMap, valueMap, true);
            Class clazz = new AnonymousClassLoader(GeneratorThatWorks.class).loadClass(patch);
            MethodType desc1 = MethodType.methodType(void.class);
            MethodHandle mh1 = MethodHandles.lookup().findConstructor(clazz, desc1);
            Object o = mh1.invoke();

            MethodType desc2 = MethodType.methodType(StringBuilder.class, StringBuilder.class, Object.class);
            MethodHandle mh2 = MethodHandles.lookup().findVirtual(clazz, "formatDouble", desc2);
            MethodHandle mh3 = MethodHandles.insertArguments(mh2, 0, o);

            StringBuilder builder = new StringBuilder();
            builder.append("running... ");
            mh3.invoke(builder, new SampleDataObject(12.0));
            System.out.println(builder.toString());

        } catch (Throwable t) {
            Logger.getLogger(GeneratorThatWorks.class.getName()).log(Level.SEVERE, null, t);
        }

    }
}