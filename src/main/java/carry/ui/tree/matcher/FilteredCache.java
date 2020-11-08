package carry.ui.tree.matcher;

/**
 * @author Carry
 * @date 2020/8/18
 */
public interface FilteredCache<E> {
    
    boolean match(E e);
    
    void clear();
    
}
