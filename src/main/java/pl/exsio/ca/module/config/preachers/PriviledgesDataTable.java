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
package pl.exsio.ca.module.config.preachers;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Locale;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherPriviledge;
import pl.exsio.ca.model.Priviledge;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.util.CalendarUtil;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.priviledges")
public class PriviledgesDataTable extends JPADataTable<PreacherPriviledge, Form> {

    protected CaEntityFactory caEntities;

    protected Preacher preacher;

    public PriviledgesDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(PriviledgesDataTable.class) {
            {
                setColumnHeaders("priviledge", "priviledge_start_date", "priviledge_end_date", "id");
                setVisibleColumns("priviledge", "startDate", "endDate", "id");
            }
        }, security);
    }

    @Override
    protected void doInit() {
        super.doInit();
        if (this.preacher == null) {
            this.setEnabled(false);
        }
        this.setHeight("250px");
        Converter dateConverter = new StringToDateConverter() {
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        this.dataComponent.setConverter("startDate", dateConverter);
        this.dataComponent.setConverter("endDate", dateConverter);
    }

    @Override
    protected JPAContainer<PreacherPriviledge> createContainer() {
        JPAContainer<PreacherPriviledge> container = super.createContainer();
        container.addContainerFilter(eq("preacher", this.preacher));
        return container;
    }

    @Override
    protected HorizontalLayout decorateControls(HorizontalLayout controls) {
        controls.removeComponent(this.editButton);
        return super.decorateControls(controls);
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends PreacherPriviledge> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        Field priviledge = ComponentFactory.createEnumComboBox(t("priviledge"), Priviledge.class);
        priviledge.setPropertyDataSource(item.getItemProperty("priviledge"));
        priviledge.addValidator(new NullValidator(t("not_null"), false));
        form.addField("priviledge", priviledge);

        DateField start = new DateField(t("start_date"));
        start.setPropertyDataSource(item.getItemProperty("startDate"));
        start.setResolution(Resolution.DAY);
        start.addValidator(new NullValidator(t("invalid_start_date"), false));
        start.setDateFormat(CalendarUtil.getDateFormat(this.getLocale()));
        form.addField("startDate", start);

        DateField end = new DateField(t("end_date"));
        end.setPropertyDataSource(item.getItemProperty("endDate"));
        end.setResolution(Resolution.DAY);
        end.setDateFormat(CalendarUtil.getDateFormat(this.getLocale()));
        form.addField("endDate", end);

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        return formLayout;
    }

    @Override
    protected float getControlsExpandRatio() {
        return 1.4f;
    }

    @Override
    protected <S extends PreacherPriviledge> Class<S> getEntityClass() {
        return this.caEntities.getPreacherPriviledgeClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void beforeDataAddition(Form form, EntityItem<? extends PreacherPriviledge> item, JPAContainer<PreacherPriviledge> container) {
        item.getItemProperty("preacher").setValue(this.preacher);
    }

    public void setPreacher(Preacher preacher) {
        this.preacher = preacher;
    }

}
