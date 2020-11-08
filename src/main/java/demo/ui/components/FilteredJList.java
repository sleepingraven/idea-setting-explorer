package demo.ui.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Carry
 * @date 2020/7/31
 */
public class FilteredJList<E, P> extends JList<E> {
    
    public FilteredJList(MyFilter<E, P> filter, E... elements) {
        setModel(new FilterListModel(filter, elements));
    }
    
    @Override
    public void setModel(ListModel<E> model) {
        if (!(model instanceof FilteredJList.FilterListModel)) {
            throw new IllegalArgumentException();
        }
        super.setModel(model);
    }
    
    public FilterListModel getFilterModel() {
        return (FilterListModel) super.getModel();
    }
    
    public void doFilter(P term) {
        getFilterModel().doFilter(term);
    }
    
    /**
     * the FilterListModel
     */
    public class FilterListModel extends AbstractListModel<E> {
        final List<E> items;
        final List<E> filterItems;
        final MyFilter<E, P> filter;
        
        public FilterListModel(MyFilter<E, P> filter, E... elements) {
            this.filter = filter;
            this.items = new ArrayList<>(Arrays.asList(elements));
            this.filterItems = new ArrayList<>(items);
        }
        
        @Override
        public E getElementAt(int index) {
            if (index < filterItems.size()) {
                return filterItems.get(index);
            } else {
                return null;
            }
        }
        
        @Override
        public int getSize() {
            return filterItems.size();
        }
        
        private void doFilter(P term) {
            filterItems.clear();
            for (E item : items) {
                if (filter.match(item, term)) {
                    filterItems.add(item);
                }
            }
            
            // 通知 JList 的视图更新显示数据
            // 它会产生 ListDataEvent 事件，使 JList 重新从模型中获取数据并重绘
            fireContentsChanged(this, 0, getSize());
        }
        
    }
    
    public static void main(String[] args) {
        String[] listItems = { "Chris", "Joshua", "Daniel", "Michael", "Don", "Kimi", "Kelly", "Keagan" };
        
        FilteredJList<String, String> jList = new FilteredJList<>(String::contains, listItems);
        JTextField jTextField = new JTextField();
        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                jList.doFilter(jTextField.getText());
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                jList.doFilter(jTextField.getText());
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                jList.doFilter(jTextField.getText());
            }
        });
        
        JScrollPane jScrollPane = new JScrollPane(jList);
        JFrame frame = new JFrame("FilteredJList");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(jTextField, BorderLayout.NORTH);
        
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * the Filter
     */
    public interface MyFilter<E, P> {
        
        /**
         * match to do filter
         *
         * @param element
         *         the current element of the list
         * @param pattern
         *         current pattern to match
         *
         * @return whether the element matches the pattern
         */
        boolean match(E element, P pattern);
        
    }
    
}
