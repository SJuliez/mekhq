/*
 * Copyright (c) 2024 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MekHQ.
 *
 * MekHQ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MekHQ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MekHQ. If not, see <http://www.gnu.org/licenses/>.
 */
package mekhq.gui.panes.campaignOptions;

import megamek.logging.MMLogger;
import mekhq.MekHQ;
import mekhq.campaign.CampaignPreset;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.util.ResourceBundle;

import static megamek.client.ui.WrapLayout.wordWrap;

/**
 * A dialog for selecting campaign presets. Extends {@link JDialog}.
 * Keeps track of the selected preset and return state.
 * Provides options to select a preset, customize a preset, or cancel the operation.
 */
public class SelectPresetDialog extends JDialog {
    private static String RESOURCE_PACKAGE = "mekhq/resources/NEWCampaignOptionsDialog";
    private static final ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_PACKAGE,
        MekHQ.getMHQOptions().getLocale());

    final static String OPTION_SELECT_PRESET = resources.getString("presetDialogSelect.name");
    final static String OPTION_CUSTOMIZE_PRESET = resources.getString("presetDialogCustomize.name");
    final static String OPTION_CANCEL = resources.getString("presetDialogCancel.name");

    private static final MMLogger logger = MMLogger.create(SelectPresetDialog.class);

    private int returnState;
    public static final int PRESET_SELECTION_CANCELLED = 0;
    public static final int PRESET_SELECTION_SELECT = 1;
    public static final int PRESET_SELECTION_CUSTOMIZE = 2;
    private CampaignPreset selectedPreset;

    /**
     * Returns the current return state of the dialog.
     * <p>{@code PRESET_SELECTION_CANCELLED} = 0
     * <p>{@code PRESET_SELECTION_SELECT} = 1
     * <p>{@code PRESET_SELECTION_CUSTOMIZE} = 2
     *
     * @return An integer representing the return state of the dialog.
     */
    public int getReturnState() {
        return returnState;
    }

    /**
     * @return The {@link CampaignPreset} that was selected.
     */
    public CampaignPreset getSelectedPreset() {
        return selectedPreset;
    }

    /**
     * Constructs a dialog window for selecting a campaign preset.
     *
     * @param frame                     the parent {@link JFrame} for the dialog
     * @param includePresetSelectOption whether to include the option to select a preset
     * @param includeCustomizePresetOption whether to include the option to customize a preset
     */
    public SelectPresetDialog(JFrame frame, boolean includePresetSelectOption, boolean includeCustomizePresetOption) {
        super(frame, resources.getString("presetDialog.title"), true);
        returnState = PRESET_SELECTION_CANCELLED;

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        ImageIcon image = new ImageIcon("data/images/misc/megamek-splash.png");
        JLabel imageLabel = new JLabel(image);
        add(imageLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        final GroupLayout layout = new GroupLayout(centerPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        centerPanel.setLayout(layout);

        JLabel descriptionLabel = new JLabel(String.format(
            "<html><body><div style='width:400px;'><center>%s</center></div></body></html>",
            resources.getString("presetDialog.description")));

        final DefaultListModel<CampaignPreset> campaignPresets = new DefaultListModel<>();
        campaignPresets.addAll(CampaignPreset.getCampaignPresets());

        if (campaignPresets.isEmpty()) {
            logger.error("No campaign presets found", "Error");
        }

        JComboBox<CampaignPreset> comboBox = new JComboBox<>();
        comboBox.setModel(convertPresetListModelToComboBoxModel(campaignPresets));

        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                if (value instanceof CampaignPreset preset) {
                    setText(preset.getTitle());
                    setToolTipText(wordWrap(preset.getDescription()));
                }

                setHorizontalAlignment(JLabel.CENTER);

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                return this;
            }
        };
        comboBox.setRenderer(listRenderer);

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(descriptionLabel)
                .addComponent(comboBox)
        );

        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addComponent(descriptionLabel)
                .addComponent(comboBox)
        );

        add(centerPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();

        JButton buttonSelect = new JButton(OPTION_SELECT_PRESET);
        buttonSelect.setToolTipText(resources.getString("presetDialogSelect.tooltip"));
        buttonSelect.addActionListener(e -> {
            selectedPreset = (CampaignPreset) comboBox.getSelectedItem();
            returnState = PRESET_SELECTION_SELECT;
            dispose();
        });
        buttonSelect.setEnabled(includePresetSelectOption);
        buttonPanel.add(buttonSelect);

        JButton buttonCustomize = new JButton(OPTION_CUSTOMIZE_PRESET);
        buttonCustomize.setToolTipText(resources.getString("presetDialogCustomize.tooltip"));
        buttonCustomize.addActionListener(e -> {
            selectedPreset = (CampaignPreset) comboBox.getSelectedItem();
            returnState = PRESET_SELECTION_CUSTOMIZE;
            dispose();
        });
        buttonCustomize.setEnabled(includeCustomizePresetOption);
        buttonPanel.add(buttonCustomize);

        JButton buttonCancel = new JButton(OPTION_CANCEL);
        buttonCancel.setToolTipText(resources.getString("presetDialogCancel.tooltip"));
        buttonCancel.addActionListener(e -> {
            returnState = PRESET_SELECTION_CANCELLED;
            dispose();
        });
        buttonPanel.add(buttonCancel);

        add(buttonPanel, BorderLayout.PAGE_END);

        pack();
        setAlwaysOnTop(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Converts a {@link DefaultListModel} of {@link CampaignPreset} objects to a {@link DefaultComboBoxModel}.
     *
     * @param listModel The {@link DefaultListModel} to convert.
     * @return The converted {@link DefaultComboBoxModel}.
     */
    private DefaultComboBoxModel<CampaignPreset> convertPresetListModelToComboBoxModel(
        DefaultListModel<CampaignPreset> listModel) {

        // Create a new DefaultComboBoxModel
        DefaultComboBoxModel<CampaignPreset> comboBoxModel = new DefaultComboBoxModel<>();

        // Populate the DefaultComboBoxModel with the elements from the DefaultListModel
        for (int i = 0; i < listModel.size(); i++) {
            comboBoxModel.addElement(listModel.get(i));
        }

        return comboBoxModel;
    }
}