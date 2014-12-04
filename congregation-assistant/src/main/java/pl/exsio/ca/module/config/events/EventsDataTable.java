/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
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
package pl.exsio.ca.module.config.events;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.AclSubjectDataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.events")
public class EventsDataTable extends AclSubjectDataTable<pl.exsio.ca.model.Event, Form> {

    protected CaEntityFactory caEntities;

    protected ApplicationEventPublisher aep;

    protected CaRepositoryProvider caRepositories;

    public EventsDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(EventsDataTable.class) {
            {
                setColumnHeaders("name", "start_date", "end_date", "id");
                setVisibleColumns("name", "startDate", "endDate", "id");
            }
        }, security);
        this.flexibleControls = true;
    }

    @Override
    protected void doInit() {
        super.doInit();
        Converter dateConverter = new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };

        this.dataComponent.setConverter("startDate", dateConverter);
        this.dataComponent.setConverter("endDate", dateConverter);
        this.dataComponent.sort(new Object[]{"startDate"}, new boolean[]{false});
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends pl.exsio.ca.model.Event> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends pl.exsio.ca.model.Event> ff = new FramesetFieldFactory<>(this.caEntities.getEventClass(), this.getClass());
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"name", "startDate", "endDate"}));
        form.setBuffered(true);
        form.setEnabled(true);
        formLayout.addComponent(form);
        Validator notNull = new NullValidator(t("not_null"), false);
        form.getField("name").addValidator(notNull);
        form.getField("startDate").addValidator(notNull);
        ((DateField) form.getField("startDate")).setDateFormat("yyyy-MM-dd");
        form.getField("endDate").addValidator(notNull);
        ((DateField) form.getField("endDate")).setDateFormat("yyyy-MM-dd");
        return formLayout;
    }

    @Override
    protected <S extends pl.exsio.ca.model.Event> Class<S> getEntityClass() {
        return this.caEntities.getEventClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

}
