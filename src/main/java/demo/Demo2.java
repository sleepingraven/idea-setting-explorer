package demo;

import lombok.ToString;

import java.util.function.Supplier;

/**
 * @author Carry
 * @date 2020/9/1
 */
public class Demo2 extends Sup implements SupI {
    Obj obj1;
    
    final Supplier<?>[] suppliers = new Supplier[10];
    int i;
    
    public Demo2() {
        suppliers[i++] = () -> obj1.toString();
        suppliers[i++] = SupI.obj2::toString;
        suppliers[i++] = super.obj2::toString;
        suppliers[i++] = obj1::toString;
    }
    
    void display() {
        for (Supplier<?> supplier : suppliers) {
            if (supplier == null) {
                return;
            }
            System.out.println(supplier.get());
        }
    }
    
    public static void main(String[] args) {
        Demo2 demo2 = new Demo2();
        demo2.display();
    }
    
}

@ToString
class Obj {
    int i = 0;
    
}

class Sup {
    Obj obj2 = new Obj();
    
}

interface SupI {
    Obj obj2 = new Obj();
    
}
