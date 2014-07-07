package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.TerrainImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class TerrainEntityProvider extends TransactionalEntityProvider {

    protected TerrainEntityProvider() {
        super(TerrainImpl.class);
    }
}
