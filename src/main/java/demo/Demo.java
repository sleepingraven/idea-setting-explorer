package demo;

/**
 * @date 2020/8/5
 */
public class Demo implements A {
    
    @Override
    public String doSomething() {
        return null;
    }
    
}

interface A {
    
    String doSomething();
    
}

interface B {
    
    void doSomething();
    
}
