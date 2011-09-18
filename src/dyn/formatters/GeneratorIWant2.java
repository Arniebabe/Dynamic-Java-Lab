package dyn.formatters;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.invoke.anon.AnonymousClassLoader;
import sun.invoke.anon.ConstantPoolPatch;
import sun.invoke.anon.InvalidConstantPoolFormatException;

/**
 * The possibilities offered by AnonymousClassLoader to path the ConstantPool look very much like good old Unix's fork()
 * system call : in principle, only what gets changed needs to be duplicated. I suspect a smart JVM implementation
 * could keep the same execution code and only duplicate Constant Pools.
 * @author acormier
 */
public class GeneratorIWant2 {

    public static MethodHandle prepare(Class acls, String methodName) throws IOException, NoSuchMethodException, IllegalAccessException {
        MethodHandle result;

        ConstantPoolPatch patch = null;
        try {
            patch = new ConstantPoolPatch(BasicFormatterTemplateIWant2.class);
            HashMap<String, String> utf8Map = new HashMap<>();
            HashMap<String, Object> classMap = new HashMap<>();
            HashMap<Object, Object> valueMap = new HashMap<>();
            valueMap.put(DataObjectTemplate.class, acls);

            patch.putPatches(utf8Map, classMap, valueMap, true);
        } catch (InvalidConstantPoolFormatException ex) {
            Logger.getLogger(GeneratorIWant2.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("patches = " + patch);

        Class clazz = new AnonymousClassLoader(GeneratorIWant2.class).loadClass(patch);
        MethodType desc1 = MethodType.methodType(void.class, MethodHandle.class);
        MethodHandle mh1 = MethodHandles.lookup().findConstructor(clazz, desc1);
        MethodType descx = MethodType.methodType(Double.class);
        MethodHandle mhArg = MethodHandles.lookup().findVirtual(acls, methodName, descx);
        Object o=null;
        try {
            o = mh1.invoke(mhArg);
        } catch (Throwable ex) {
            Logger.getLogger(GeneratorIWant2.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("running...");


        MethodType desc3v1 = MethodType.methodType(StringBuilder.class, StringBuilder.class, Object.class);
        // yeah, ok... Shouldn't hope for the patch to simply patch *every* occurrence of DataObjectTemplate by SampleDataObject...
        MethodHandle mh2v1 = MethodHandles.lookup().findVirtual(clazz, "formatDouble", desc3v1);
        result = MethodHandles.insertArguments(mh2v1, 0, o);

        System.out.println("mh2v1 = " + mh2v1);
        System.out.println("result = " + result);
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {
        HashMap<Class, MethodHandle> formatters = new HashMap<>();
        try {
            formatters.put(SampleDataObject.class, prepare(SampleDataObject.class, "getValue"));
        } catch (IOException | NoSuchMethodException | IllegalAccessException ex) {
            Logger.getLogger(GeneratorIWant2.class.getName()).log(Level.SEVERE, null, ex);
        }

        StringBuilder builder= new StringBuilder();
        formatters.get(SampleDataObject.class).invoke(builder, new SampleDataObject(12.0));
        System.out.println(builder.toString());


    }
}
