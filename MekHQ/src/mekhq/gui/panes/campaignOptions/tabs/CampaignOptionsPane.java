package mekhq.gui.panes.campaignOptions.tabs;

import megamek.logging.MMLogger;
import mekhq.campaign.Campaign;
import mekhq.gui.baseComponents.AbstractMHQTabbedPane;

import javax.swing.*;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Math.round;
import static mekhq.gui.panes.campaignOptions.tabs.CampaignOptionsUtilities.createSubTabs;

public class CampaignOptionsPane extends AbstractMHQTabbedPane {
    private static final MMLogger logger = MMLogger.create(CampaignOptionsPane.class);
    private static final String RESOURCE_PACKAGE = "mekhq/resources/NEWCampaignOptionsDialog";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE);

    private static final int SCROLL_SPEED = 16;

    private final Campaign campaign;

    public CampaignOptionsPane(final JFrame frame, final Campaign campaign) {
        super(frame, resources, "campaignOptionsDialog");
        this.campaign = campaign;

        initialize();
    }

    @Override
    protected void initialize() {
        int fontSize = 5;
        double uiScale = Double.parseDouble(System.getProperty("flatlaf.uiScale"));

        addTab(String.format("<html><font size=%s><b>%s</b></font></html>", round(fontSize * uiScale),
            resources.getString("generalPanel.title")), createGeneralTab());

        createTab("combatReadinessParentTab", createCombatReadinessParentTab());
        createTab("humanResourcesParentTab", createHumanResourcesParentTab());
        createTab("unitDevelopmentParentTab", createUnitDevelopmentParentTab());
        createTab("logisticsAndMaintenanceParentTab", createLogisticsAndMaintenanceParentTab());
        createTab("strategicOperationsParentTab", createStrategicOperationsParentTab());
    }

    /**
     * Creates a tab and adds it to the TabbedPane.
     *
     * @param resourceName the name of the resource used to create the tab's title
     * @param tab          the tab to be added
     */
    private void createTab(String resourceName, JTabbedPane tab) {
        JScrollPane tabScrollPane = new JScrollPane(tab);

        // Increase scroll speed
        tabScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        tabScrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);

        // Dynamically adjust font size based on the GUI scale
        int fontSize = 5;
        double uiScale = Double.parseDouble(System.getProperty("flatlaf.uiScale"));

        addTab(String.format("<html><font size=%s><b>%s</b></font></html>", round(fontSize * uiScale),
            resources.getString(resourceName + ".title")), tabScrollPane);
    }

    /**
     * Creates the General tab.
     *
     * @return a {@link JScrollPane} containing the General tab
     */
    private JScrollPane createGeneralTab() {
        GeneralTab generalTab = new GeneralTab(campaign, getFrame(), "generalTab");
        JPanel createdGeneralTab = generalTab.createGeneralTab();

        return new JScrollPane(createdGeneralTab);
    }

    private JTabbedPane createCombatReadinessParentTab() {
        // Tech Limits
        // Random Assignment Tables
        // Rulesets

        // Parent Tab
        JTabbedPane combatReadinessParentTab = new JTabbedPane();

        TechLimitsTab techLimitsTab = new TechLimitsTab(getFrame(),
            "techLimitsTab");

        combatReadinessParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("TechLimitsParentTab.title")), techLimitsTab.createTechLimitsTab());

        return combatReadinessParentTab;
    }

    /**
     * The `createHumanResourcesParentTab` method creates and returns a `JTabbedPane` object,
     * which represents the overarching "Human Resources" tab in the user interface.
     * <p>
     * Under the "Human Resources" tab, there are several sub-tabs for different categories, including
     * Personnel, Biography, Relationships, and Turnover and Retention. Each sub-tab contains various
     * settings related to its category.
     *
     * @return JTabbedPane representing the "Human Resources" tab.
     */
    private JTabbedPane createHumanResourcesParentTab() {
        // Parent Tab
        JTabbedPane humanResourcesParentTab = new JTabbedPane();

        // Personnel
        PersonnelTab personnelTab = new PersonnelTab(getFrame(), "personnelTab");

        JTabbedPane personnelContentTabs = createSubTabs(Map.of(
            "personnelGeneralTab", personnelTab.createGeneralTab(),
            "personnelLogsTab", personnelTab.createPersonnelLogsTab(),
            "personnelInformationTab", personnelTab.createPersonnelInformationTab(),
            "administratorsTab", personnelTab.createAdministratorsTab(),
            "awardsTab", personnelTab.createAwardsTab(),
            "prisonersAndDependentsTab", personnelTab.createPrisonersAndDependentsTab(),
            "medicalTab", personnelTab.createMedicalTab(),
            "salariesTab", personnelTab.createSalariesTab()));

        // Biography
        BiographyTab biographyTab = new BiographyTab(campaign, getFrame(), "biographyTab");

        JTabbedPane biographyContentTabs = createSubTabs(Map.of(
            "biographyGeneralTab", biographyTab.createGeneralTab(),
            "backgroundsTab", biographyTab.createBackgroundsTab(),
            "deathTab", biographyTab.createDeathTab(),
            "educationTab", biographyTab.createEducationTab(),
            "nameAndPortraitGenerationTab", biographyTab.createNameAndPortraitGenerationTab(),
            "rankTab", biographyTab.createRankTab()));

        // Relationships
        RelationshipsTab relationshipsTab = new RelationshipsTab(getFrame(), "relationshipsTab");

        JTabbedPane relationshipsContentTabs = createSubTabs(Map.of(
            "marriageTab", relationshipsTab.createMarriageTab(),
            "divorceTab", relationshipsTab.createDivorceTab(),
            "procreationTab", relationshipsTab.createProcreationTab()));

        // Turnover and Retention
        TurnoverAndRetentionTab turnoverAndRetentionTab = new TurnoverAndRetentionTab(getFrame(),
            "turnoverAndRetentionTab");

        JTabbedPane turnoverAndRetentionContentTabs = createSubTabs(Map.of(
            "turnoverTab", turnoverAndRetentionTab.createTurnoverTab(),
            "fatigueTab", turnoverAndRetentionTab.createFatigueTab()));

        // Add Tabs
        humanResourcesParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("personnelContentTabs.title")), personnelContentTabs);
        humanResourcesParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("biographyContentTabs.title")), biographyContentTabs);
        humanResourcesParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("relationshipsContentTabs.title")), relationshipsContentTabs);
        humanResourcesParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("turnoverAndRetentionContentTabs.title")), turnoverAndRetentionContentTabs);

        addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("humanResourcesParentTab.title")), humanResourcesParentTab);

        return humanResourcesParentTab;
    }

    private JTabbedPane createUnitDevelopmentParentTab() {
        //    Experience
        //    Skills
        //    Skill Randomization
        //    SPAs

        // Parent Tab
        JTabbedPane unitDevelopmentParentTab = new JTabbedPane();

        return unitDevelopmentParentTab;
    }

    /**
     * This `createLogisticsAndMaintenanceParentTab` method creates and returns a `JTabbedPane` object.
     * This represents the "Logistics and Maintenance" parent tab in the user interface.
     *
     * @return JTabbedPane representing the "Logistics and Maintenance" tab.
     */
    private JTabbedPane createLogisticsAndMaintenanceParentTab() {
        // Parent Tab
        JTabbedPane logisticsAndMaintenanceParentTab = new JTabbedPane();

        // Repair and Maintenance
        RepairAndMaintenanceTab repairAndMaintenanceTab = new RepairAndMaintenanceTab(getFrame(),
            "repairAndMaintenanceTab");

        JTabbedPane repairAndMaintenanceContentTabs = createSubTabs(Map.of(
            "repairTab", repairAndMaintenanceTab.createRepairTab(),
            "maintenanceTab", repairAndMaintenanceTab.createMaintenanceTab()));

        // Supplies and Acquisition
        SuppliesAndAcquisitionTab suppliesAndAcquisitionTab = new SuppliesAndAcquisitionTab(getFrame(),
            "suppliesAndAcquisitionTab");

        JTabbedPane suppliesAndAcquisitionContentTabs = createSubTabs(Map.of(
            "acquisitionTab", suppliesAndAcquisitionTab.createAcquisitionTab(),
            "deliveryTab", suppliesAndAcquisitionTab.createDeliveryTab(),
            "planetaryAcquisitionTab", suppliesAndAcquisitionTab.createPlanetaryAcquisitionTab()));

        // Add tabs
        logisticsAndMaintenanceParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("suppliesAndAcquisitionTab.title")), suppliesAndAcquisitionContentTabs);
        logisticsAndMaintenanceParentTab.addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("repairAndMaintenanceContentTabs.title")), repairAndMaintenanceContentTabs);

        addTab(String.format("<html><font size=%s><b>%s</b></font></html>", 4,
            resources.getString("logisticsAndMaintenanceParentTab.title")), logisticsAndMaintenanceParentTab);

        return logisticsAndMaintenanceParentTab;
    }

    private JTabbedPane createStrategicOperationsParentTab() {
        //    Finances
        //    Mercenary
        //    Markets

        // Parent Tab
        JTabbedPane strategicOperationsParentTab = new JTabbedPane();

        return strategicOperationsParentTab;
    }

    private void setOptions() {
        // TODO this is where we update the dialog based on current campaign settings.
    }

    private void updateOptions() {
        // TODO this is where we update campaign values based on the dialog values
    }
}