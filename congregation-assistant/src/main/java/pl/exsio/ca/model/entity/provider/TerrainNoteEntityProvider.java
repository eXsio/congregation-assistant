package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.TerrainNoteImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class TerrainNoteEntityProvider extends TransactionalEntityProvider {

    protected TerrainNoteEntityProvider() {
        super(TerrainNoteImpl.class);
    }
}
