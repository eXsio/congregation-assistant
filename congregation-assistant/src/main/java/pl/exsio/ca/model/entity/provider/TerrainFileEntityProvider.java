package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.TerrainFileImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class TerrainFileEntityProvider extends TransactionalEntityProvider {

    protected TerrainFileEntityProvider() {
        super(TerrainFileImpl.class);
    }
}
