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
public class GeneratorIWant {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ConstantPoolPatch patch = new ConstantPoolPatch(BasicFormatterTemplateIWant.class);
            HashMap<String, String> utf8Map = new HashMap<>();
            utf8Map.put("RequestTemplate", "SampleDataObject");
            utf8Map.put("getDoubleProperty", "getValue");

            HashMap<String, Object> classMap = new HashMap<>();
            classMap.put("RequestTemplate", SampleDataObject.class);
            HashMap<Object, Object> valueMap = new HashMap<>();
            valueMap.put(DataObjectTemplate.class, SampleDataObject.class);

            patch.putPatches(utf8Map, classMap, valueMap, true);
            System.out.println("patches = " + patch);
            
            Class clazz = new AnonymousClassLoader(GeneratorIWant.class).loadClass(patch);
            MethodType desc1 = MethodType.methodType(void.class);
            MethodHandle mh1 = MethodHandles.lookup().findConstructor(clazz, desc1);
            Object o = mh1.invoke();
            StringBuilder builder = new StringBuilder();
            builder.append("running...");


            try { // Approach 1 -> Naïve
                MethodType desc3v1 = MethodType.methodType(StringBuilder.class, StringBuilder.class, SampleDataObject.class);
                // yeah, ok... Shouldn't hope for the patch to simply patch *every* occurrence of DataObjectTemplate by SampleDataObject...
                MethodHandle mh2v1 = MethodHandles.lookup().findVirtual(clazz, "formatDouble", desc3v1);
                MethodHandle mh3v1 = MethodHandles.insertArguments(mh2v1, 0, o);

                System.out.println("mh2v1 = " + mh2v1);
                System.out.println("mh3v1 = " + mh3v1);
                mh3v1.invoke(builder, new SampleDataObject(12.0));

            } catch (Throwable t) {
                Logger.getLogger(GeneratorIWant.class.getName()).log(Level.SEVERE, null, t);
            }

            try { // Approach 2 -> Less naïve, but not more successful ... :(

                MethodType desc2v2 = MethodType.methodType(StringBuilder.class, StringBuilder.class, DataObjectTemplate.class);
                MethodHandle mh2v2 = MethodHandles.lookup().findVirtual(clazz, "formatDouble", desc2v2);
                MethodType desc3v2 = MethodType.methodType(StringBuilder.class, StringBuilder.class, SampleDataObject.class);
                MethodHandle mh3v2 = MethodHandles.insertArguments(mh2v2, 0, o);
                MethodHandle mh4v2 = MethodHandles.explicitCastArguments(mh3v2, desc3v2);

                System.out.println("mh2v2 = " + mh2v2);
                System.out.println("mh3v2 = " + mh3v2);
                System.out.println("mh3v2 = " + mh4v2);

                try {
                    // why, but why in hell won't it let me change DataObjectTemplate for SampleDataObject ?
                    mh4v2.invoke(builder, new SampleDataObject(12.0));
                } catch (Throwable t) {
                    Logger.getLogger(GeneratorIWant.class.getName()).log(Level.SEVERE, null, t);
                }

                try {
                    // this was never meant to work, but the resulting exception seems to indicate class clazz has become inconsistent
                    mh4v2.invoke(builder, new DataObjectTemplate());
                } catch (Throwable t) {
                    Logger.getLogger(GeneratorIWant.class.getName()).log(Level.SEVERE, null, t);
                }            
            
            } catch (Throwable t) {
                Logger.getLogger(GeneratorIWant.class.getName()).log(Level.SEVERE, null, t);
            }

            System.out.println(builder.toString());

        } catch (Throwable t) {
            Logger.getLogger(GeneratorIWant.class.getName()).log(Level.SEVERE, null, t);
        }

    }
}
