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
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.frameset.security.context.provider.SecurityContextProvider;
import pl.exsio.frameset.vaadin.module.VerticalModule;

/**
 *
 * @author exsio
 */
public class CaTerrainEvidenceModule extends VerticalModule {

    private transient CaEntityProviderProvider caEntityProviders;

    @Autowired
    private transient ApplicationEventPublisher aep;

    private transient CaEntityFactory caEntities;

    private transient CaRepositoryProvider caRepositories;

    public CaTerrainEvidenceModule() {
        this.setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        TerrainDataTable table = this.createTerrainDataTable();
        table.setCaEntities(this.caEntities);
        table.setEntityProvider(this.caEntityProviders.getTerrainEntityProvider());
        table.setApplicationEventPublisher(this.aep);
        table.setCaRepositories(this.caRepositories);
        table.setCaEntityProviders(this.caEntityProviders);
        this.addComponent(table.init());
        this.setMargin(true);
    }

    protected TerrainDataTable createTerrainDataTable() {
        return new TerrainDataTable(SecurityContextProvider.getFor(this.frame));
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider entityProviders) {
        this.caEntityProviders = entityProviders;
    }

}
