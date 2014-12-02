/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.report;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.ca.module.terrain.report.registry.ReportRegistry;
import pl.exsio.frameset.vaadin.module.VerticalModule;

/**
 *
 * @author exsio
 */
public class CaReportModule extends VerticalModule {

    protected transient ReportRegistry reportRegistry;

    protected transient Report lastReport;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        this.setMargin(true);
        this.setSpacing(true);
        this.setSizeFull();
        ComboBox chooser = this.createReportChooser();
        this.addComponent(chooser);
        this.setExpandRatio(chooser, 1);
        if (!chooser.getItemIds().isEmpty()) {
            chooser.select(chooser.getItemIds().toArray()[0]);
        }
    }

    protected ComboBox createReportChooser() {
        ComboBox chooser = new ComboBox(t("ca.report.pick"));
        final BeanItemContainer<Report> container = new BeanItemContainer(Report.class);
        for (Report report : this.reportRegistry.getRegisteredReports()) {
            container.addBean(report);
        }
        chooser.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        chooser.setItemCaptionPropertyId("name");
        chooser.setContainerDataSource(container);
        chooser.setWidth("300px");
        chooser.setNullSelectionAllowed(false);
        chooser.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    if (lastReport instanceof Report) {
                        removeComponent(lastReport);
                    }
                    lastReport = container.getItem(event.getProperty().getValue()).getBean();
                    addComponent(lastReport.load());
                    setExpandRatio(lastReport, 20);
                }
            }
        });

        return chooser;
    }

    public void setReportRegistry(ReportRegistry reportRegistry) {
        this.reportRegistry = reportRegistry;
    }

}
