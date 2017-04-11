package wiki.chenxun.ace.core.base.support;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * @Description: 反射工具类
 * Created by chenxun on 2017/4/9.
 */
public final class ReflectUtil {


    private ReflectUtil() {
    }

    /**
     * 使用字节码工具ASM来获取方法的参数名
     *  FIXME  此函数在Web环境中有可能得不到ClassReader
     * @param method 方法
     * @return 参数名列表
     * @throws IOException io异常
     */
    public static List<MethodParameter> getMethodParamNames(final Method method) throws IOException {

        final String methodName = method.getName();
        final Class<?>[] methodParameterTypes = method.getParameterTypes();
        final java.lang.reflect.Type[] genericParameterTypes = method.getGenericParameterTypes();
        final int methodParameterCount = methodParameterTypes.length;
        final String className = method.getDeclaringClass().getName();
        final boolean isStatic = Modifier.isStatic(method.getModifiers());
        final List<MethodParameter> methodParameters = new ArrayList<MethodParameter>();
        ClassReader cr = new ClassReader(className);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new ClassAdapter(cw) {
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                final Type[] argTypes = Type.getArgumentTypes(desc);

                //参数类型不一致
                if (!methodName.equals(name) || !matchTypes(argTypes, methodParameterTypes)) {
                    return mv;
                }
                return new MethodAdapter(mv) {
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        //如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
                        int methodParameterIndex = isStatic ? index : index - 1;
                        if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
                            MethodParameter methodParameter = new MethodParameter();
                            methodParameter.setMethod(method);
                            methodParameter.setParameterIndex(methodParameterIndex);
                            methodParameter.setParameterName(name);
                            methodParameter.setParameterType(methodParameterTypes[methodParameterIndex]);
                            methodParameter.setGenericParameterType(genericParameterTypes[methodParameterIndex]);
                            methodParameters.add(methodParameter);
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }
                };
            }
        }, 0);
        return methodParameters;
    }

    /**
     * 比较参数是否一致
     *
     * @param types          types
     * @param parameterTypes parameterTypes
     * @return 一致为true，否则false
     */
    private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
        if (types.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

}
