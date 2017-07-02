/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.report")
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
        ComboBox chooser = new ComboBox(t("pick"));
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
