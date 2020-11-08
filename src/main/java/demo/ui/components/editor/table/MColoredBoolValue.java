package demo.ui.components.editor.table;

/**
 * @author Carry
 * @date 2020/8/13
 */
public class MColoredBoolValue extends MColoredValue<Boolean> {
    
    public MColoredBoolValue(Boolean v, boolean colored) {
        super(v, colored);
    }
    
    @Override
    public boolean isColored() {
        return this.value;
    }
    
}
