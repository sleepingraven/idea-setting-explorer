package carry.ui.desc;

import carry.actions.ActionCenter;
import carry.actions.base.ScrollingActionsInstaller;
import carry.common.AppConstants;
import carry.common.CommonUtil;
import carry.common.ConfigsUtil;
import carry.common.entity.BaseComposite;
import carry.common.visitors.ApplicationServiceAccessor;
import carry.ui.base.HierarchyContainer;
import carry.ui.menus.andtoolbars.MainPopupMenu;
import carry.ui.tree.ConfigsTreeController;
import com.intellij.codeInsight.documentation.DocumentationComponent;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsUtil;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.ComponentUtil;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBHtmlEditorKit;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.JBUI.Borders;
import com.intellij.util.ui.JBUI.CurrentTheme;
import com.intellij.util.ui.UIUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument.RunElement;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Carry
 * @date 2020/8/20
 */
public class DescriptionPanel extends JBPanel<DescriptionPanel> implements HierarchyContainer, ApplicationServiceAccessor {
    private final ConfigsTreeController controller;
    
    private final JTextPane textComponent;
    private final Highlighter highlighter;
    @Getter
    private final MainPopupMenu mainPopupMenu;
    @Getter
    private final DescriptionToolbarPanel descriptionToolbarPanel;
    private final ImgSizeAdapter sizer;
    
    public DescriptionPanel(ConfigsTreeController controller, ActionCenter actionCenter) {
        this.controller = controller;
        textComponent = new JTextPane();
        highlighter = textComponent.getHighlighter();
        mainPopupMenu = new MainPopupMenu(actionCenter);
        descriptionToolbarPanel = new DescriptionToolbarPanel(actionCenter);
        JScrollPane jScrollPane = ScrollPaneFactory.createScrollPane(textComponent, true);
        sizer = new ImgSizeAdapter(textComponent, jScrollPane);
        
        textComponent.setEditable(false);
        textComponent.setContentType("text/html");
        textComponent.setBackground(EditorColorsUtil.getGlobalOrDefaultColor(EditorColors.DOCUMENTATION_COLOR));
        textComponent.setBorder(Borders.empty(8));
        ComponentUtil.putClientProperty(jScrollPane.getVerticalScrollBar(), JBScrollPane.IGNORE_SCROLLBAR_IN_INSETS,
                                        true);
        // ?
        textComponent.setDoubleBuffered(true);
        UIUtil.doNotScrollToCaret(textComponent);
        
        setLayout(new BorderLayout());
        if (PERSISTENCE.toolbarVisible) {
            add(descriptionToolbarPanel, PERSISTENCE.toolbarLocation);
        }
        add(jScrollPane, BorderLayout.CENTER);
        
        // support hyperlink
        textComponent.addHyperlinkListener(new BrowserHyperlinkListener());
        
        HTMLEditorKit editorKit = new JBHtmlEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return new HTMLFactory() {
                    /**
                     * see {@link ImageView}'s method "updateImageSize"
                     */
                    @Override
                    public View create(Element elem) {
                        AttributeSet attrs = elem.getAttributes();
                        Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
                        Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
                        if (o instanceof Tag) {
                            Tag kind = (Tag) o;
                            if (kind == Tag.IMG) {
                                ImageView imageView = new ImageView(elem) {
                                    /**
                                     * support debugging
                                     */
                                    @Override
                                    public void paint(Graphics g, Shape a) {
                                        super.paint(g, a);
                                    }
                                };
                                sizer.add(imageView);
                                // wrong at ImageView.loadImage() in 2021.1
                                try {
                                    Hashtable<URL, Image> imageCache =
                                            (Hashtable<URL, Image>) elem.getDocument().getProperty("imageCache");
                                    if (imageCache == null) {
                                        imageCache = new Hashtable<>();
                                        elem.getDocument().putProperty("imageCache", imageCache);
                                    }
                                    final BufferedImage newImage =
                                            ImageIO.read(new URL(imageView.getImageURL().toString()));
                                    imageCache.put(imageView.getImageURL(), newImage);
                                    
                                    final String width = (String) ((RunElement) imageView.getElement()).getAttribute(
                                            Attribute.WIDTH);
                                    double r = (double) newImage.getHeight() / newImage.getWidth();
                                    int height = (int) Math.round(Integer.parseInt(width) * r);
                                    ((RunElement) imageView.getElement()).addAttribute(Attribute.HEIGHT,
                                                                                       String.valueOf(height));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return imageView;
                            }
                        }
                        return super.create(elem);
                    }
                };
            }
        };
        textComponent.setEditorKit(editorKit);
        applyStyleProps();
        
        ScrollingActionsInstaller.install(jScrollPane, this);
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (e.getComponent().isShowing()) {
                    // refresh to fix font's style when theme changed
                    applyStyleProps();
                    sizer.resetContainerSize();
                } else {
                    try {
                        // to update font's color when theme changed between darcula
                        textComponent.setPage(EMPTY);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                sizer.resetContainerSize();
                sizer.adjustAll();
            }
        });
        addMouseAdapter(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // prevent losing focus when escape at first time
                    IdeFocusManager.getInstance(controller.getProject()).requestFocus(DescriptionPanel.this, true);
                    JPopupMenu jPopupMenu = mainPopupMenu.getComponent();
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void setComposite(BaseComposite composite) {
        try {
            String dir = getDescDir(composite);
            URL resource = getClass().getResource(dir + AppConstants.DESC_DOC_NAME);
            if (resource == null) {
                resource = NO_DOC_YET;
            }
            if (!resource.equals(textComponent.getPage())) {
                sizer.clear();
            }
            textComponent.setPage(resource);
            // soft wrapping not used now
            // UIUtil.enableEagerSoftWrapping(textComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static final URL NO_DOC_YET = DescriptionPanel.class.getResource("/docs/no-doc-yet.html");
    private static final URL EMPTY = DescriptionPanel.class.getResource("/utils/empty.html");
    private static final List<String> FILE_PATH_DELIMITERS = Arrays.asList("/", "/", "/");
    
    private String getDescDir(BaseComposite composite) {
        return String.format("/docs/%s/",
                             StringUtil.replace(CommonUtil.encode(composite.getId()), ConfigsUtil.ID_SEPARATORS,
                                                FILE_PATH_DELIMITERS));
    }
    
    /**
     * make sure adjust correctly when actions perform. calls before resized
     */
    public void fireReadjust() {
        sizer.fireReadjust();
    }
    
    public void applyStyleProps() {
        String editorFontName =
                StringUtil.escapeQuotes(EditorColorsManager.getInstance().getGlobalScheme().getEditorFontName());
        String docFontName = Registry.is("documentation.component.editor.font") ? editorFontName :
                             StringUtil.escapeQuotes(textComponent.getFont().getFontName());
        int size = JBUIScale.scale(DocumentationComponent.getQuickDocFontSize().getSize());
        String color = Integer.toHexString(CurrentTheme.Label.foreground().getRGB() & 0xFFFFFF);
        
        HTMLEditorKit kit = (HTMLEditorKit) textComponent.getEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        
        // * not effect
        styleSheet.addRule(
                String.format("body, p, li, td { font-family: \"%s\"; font-size: %d; color: #%s }", docFontName, size,
                              color));
        // don't set the color now
        styleSheet.addRule(String.format("kbd, tt, code, pre { font-family: \"%s\"; }", editorFontName));
        styleSheet.addRule(String.format("kbd { font-size: %s; }", size));
        styleSheet.addRule(String.format("tt, code, pre { font-size: %s; }", "96%"));
        styleSheet.addRule(String.format("a { color: #%s; }", ColorUtil.toHex(JBUI.CurrentTheme.Link.Foreground.ENABLED)));
        
        styleSheet.addRule("ul ul { list-style-type: circle; }");
        styleSheet.addRule("ul ul ul { list-style-type: square; }");
        styleSheet.addRule("html { padding-bottom: 8px; }");
        styleSheet.addRule("h1, h2, h3, h4, h5, h6 { margin-top: 0; padding-top: 1px; }");
        styleSheet.addRule("p { padding: 1px 0 2px 0; }");
        styleSheet.addRule("ol { padding: 0 16px 0 0; }");
        styleSheet.addRule("ul { padding: 0 16px 0 0; }");
        styleSheet.addRule("li { padding: 1px 0 2px 0; }");
        
        // sections table
        styleSheet.addRule("tr { margin: 0 0 0 0; padding: 0 0 0 0; }");
        styleSheet.addRule("table p { padding-bottom: 0}");
        styleSheet.addRule("td { margin: 4px 0 0 0; padding: 0 0 0 0; }");
        styleSheet.addRule("th { text-align: left; }");
        
        // styleSheet.addRule(String.format("body { padding: %d }", 8));
    }
    
    /**
     * font is bigger; don't include color
     * should call after any page changed
     */
    @Deprecated
    public void applyFontProps1() {
        String fontName = Registry.is("documentation.component.editor.font") ?
                          EditorColorsManager.getInstance().getGlobalScheme().getEditorFontName() :
                          textComponent.getFont().getFontName();
        // isn't effect
        // FontUIResource fontWithFallback = UIUtil.getFontWithFallback(fontName, Font.PLAIN, JBUIScale.scale(
        //         DocumentationComponent.getQuickDocFontSize().getSize()));
        // textComponent.setFont(fontWithFallback);
        
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr, fontName);
        StyleConstants.setFontSize(attr, JBUIScale.scale(DocumentationComponent.getQuickDocFontSize().getSize()));
        
        StyledDocument styledDocument = textComponent.getStyledDocument();
        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), attr, true);
        textComponent.revalidate();
        textComponent.repaint();
    }
    
    @Override
    public @NotNull Stream<JComponent> getAppearances() {
        return piece(Stream.of(textComponent), descriptionToolbarPanel);
    }
    
    public void setSelectable(boolean selectable) {
        textComponent.setHighlighter(selectable ? highlighter : null);
    }
    
}
