package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.TerrainNotificationImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class TerrainNotificationEntityProvider extends TransactionalEntityProvider {

    protected TerrainNotificationEntityProvider() {
        super(TerrainNotificationImpl.class);
    }
}
