package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.TerrainAssignmentImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class TerrainAssignmentEntityProvider extends TransactionalEntityProvider {

    protected TerrainAssignmentEntityProvider() {
        super(TerrainAssignmentImpl.class);
    }
}
