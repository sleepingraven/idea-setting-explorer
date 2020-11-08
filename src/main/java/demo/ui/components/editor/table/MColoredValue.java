package demo.ui.components.editor.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Carry
 * @date 2020/8/13
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MColoredValue<T> {
    T value;
    boolean colored;
    
}
