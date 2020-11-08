package demo.ui.components;

import com.intellij.icons.AllIcons.Actions;
import com.intellij.icons.AllIcons.General;
import com.intellij.icons.AllIcons.Ide;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.AnimatedIcon.Blinking;
import com.intellij.ui.AnimatedIcon.FS;
import com.intellij.ui.AnimatedIcon.Fading;
import com.intellij.ui.JBColor;
import com.intellij.ui.RelativeFont;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.TextIcon;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.ui.components.panels.HorizontalLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Comes from the example {@link com.intellij.ui.components.TestTextFieldAction TestTextFieldAction}'s inner class.
 *
 * @author Carry
 * @date 2020/7/28
 */
public class SeveralTextFieldsDemo extends JPanel {
    private final JCheckBox columns;
    private final JCheckBox opaque;
    private final JCheckBox gradient;
    private final JComboBox<Fill> fill;
    private final JPanel control;
    private final JPanel center;
    private final List<JTextField> fields;
    
    public SeveralTextFieldsDemo() {
        super(new BorderLayout(10, 10));
        this.columns = new JCheckBox("20 columns");
        this.opaque = new JCheckBox("Opaque");
        this.gradient = new JCheckBox("Gradient");
        this.fill = new ComboBox<>(Fill.values());
        this.control = new JPanel(new HorizontalLayout(5));
        this.center = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D && gradient.isSelected()) {
                    Graphics2D g2d = (Graphics2D) g;
                    Rectangle bounds = new Rectangle(this.getWidth(), this.getHeight());
                    g2d.setPaint(new LinearGradientPaint((float) bounds.x, (float) bounds.y, (float) bounds.width,
                                                         (float) bounds.height, new float[] { 0.0F, 1.0F },
                                                         new Color[] {
                                                                 JBColor.LIGHT_GRAY, JBColor.DARK_GRAY
                                                         }));
                    g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                } else {
                    super.paintComponent(g);
                }
            }
        };
        this.fields = Arrays.asList(new JBTextField(), new JBTextField() {
                                        {
                                            this.putClientProperty("JTextField.variant", "search");
                                        }
                                    }, (new SearchTextField(false)).getTextEditor(), (new SearchTextField(true)).getTextEditor(),
                                    new ExpandableTextField(), new ExtendableTextField() {
                    {
                        this.setExtensions(new Extension() {
                            @Override
                            public Icon getIcon(boolean hovered) {
                                return General.GearPlain;
                            }
                            
                            @Override
                            public String getTooltip() {
                                return "Settings";
                            }
                            
                            @Override
                            public boolean isIconBeforeText() {
                                return true;
                            }
                        }, new Extension() {
                            @Override
                            public Icon getIcon(boolean hovered) {
                                return hovered ? General.ContextHelp : General.Locate;
                            }
                            
                            @Override
                            public String getTooltip() {
                                return "Locate";
                            }
                            
                            @Override
                            public boolean isIconBeforeText() {
                                return true;
                            }
                        }, new Extension() {
                            private final Icon icon = new FS();
                            
                            @Override
                            public Icon getIcon(boolean hovered) {
                                return !hovered ? this.icon : com.intellij.icons.AllIcons.Process.FS.Step_passive;
                            }
                            
                            @Override
                            public String getTooltip() {
                                return "Refresh";
                            }
                        }, new Extension() {
                            private final Icon fading;
                            private final Icon blinking;
                            
                            {
                                this.fading = new Fading(Ide.FatalError);
                                this.blinking = new Blinking(Ide.FatalError);
                            }
                            
                            @Override
                            public Icon getIcon(boolean hovered) {
                                return hovered ? this.fading : this.blinking;
                            }
                        }, new Extension() {
                            private final TextIcon icon = new TextIcon("empty", null, null, 1);
                            
                            @Override
                            public Icon getIcon(boolean hovered) {
                                if (null == this.getActionOnClick()) {
                                    this.icon.setFont(RelativeFont.SMALL.derive(getFont()));
                                    this.icon.setBackground(getForeground());
                                    this.icon.setForeground(getBackground());
                                    return this.icon;
                                } else {
                                    return hovered ? Actions.CloseHovered : Actions.Close;
                                }
                            }
                            
                            @Override
                            public String getTooltip() {
                                return "Clear";
                            }
                            
                            @Override
                            public Runnable getActionOnClick() {
                                return getText().isEmpty() ? null : () -> setText(null);
                            }
                        });
                    }
                });
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add("North", this.control);
        this.add("Center", this.center);
        this.control.add(this.columns);
        this.columns.setSelected(true);
        this.columns.addChangeListener((event) -> this.updateColumns());
        this.updateColumns();
        this.control.add(this.opaque);
        this.opaque.addChangeListener((event) -> this.updateOpaque());
        this.updateOpaque();
        this.control.add(this.gradient);
        this.gradient.addChangeListener((event) -> this.updateGradient());
        this.updateGradient();
        this.control.add(new JLabel("Fill:"));
        this.control.add(this.fill);
        this.fill.addItemListener((event) -> this.updateFill());
        this.updateFill();
    }
    
    private void updateColumns() {
        int amount = this.columns.isSelected() ? 20 : 0;
        this.update((field) -> field.setColumns(amount));
    }
    
    private void updateOpaque() {
        boolean state = this.opaque.isSelected();
        this.update((field) -> field.setOpaque(state));
    }
    
    private void updateGradient() {
        this.update((field) -> {
        });
    }
    
    private void updateFill() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.weightx = 1.0D;
        gbc.weighty = 1.0D;
        gbc.anchor = 10;
        gbc.fill = this.fill.getSelectedIndex();
        if (gbc.fill < 0) {
            gbc.fill = 0;
        }
        
        this.center.removeAll();
        this.update((field) -> this.center.add(field, gbc));
    }
    
    private void update(Consumer<? super JTextField> consumer) {
        this.fields.forEach(consumer);
        this.center.revalidate();
        this.center.repaint();
    }
    
    private enum Fill {
        None, Both, Horizontal, Vertical;
    }
    
}
