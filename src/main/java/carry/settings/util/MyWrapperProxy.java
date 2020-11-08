package carry.settings.util;

import lombok.Setter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author Carry
 * @date 2020/9/1
 */
public class MyWrapperProxy<T> {
    private final Enhancer enhancer;
    @Setter
    private T wrapped;
    
    public MyWrapperProxy(Class<T> clazz) {
        enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> method.invoke(wrapped, objects));
    }
    
    public T createProxy() {
        return (T) enhancer.create();
    }
    
}
