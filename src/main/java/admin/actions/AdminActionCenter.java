package admin.actions;

/**
 * @author Carry
 * @date 2020/9/3
 */
public class AdminActionCenter {
    public final PopulateXmlDataAction populateXmlData;
    public final GenerateResourceAction generateResource;
    
    public AdminActionCenter() {
        populateXmlData = new PopulateXmlDataAction();
        generateResource = new GenerateResourceAction();
    }
    
}
