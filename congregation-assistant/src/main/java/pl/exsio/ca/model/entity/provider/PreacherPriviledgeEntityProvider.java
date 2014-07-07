package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.PreacherPriviledgeImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class PreacherPriviledgeEntityProvider extends TransactionalEntityProvider {

    protected PreacherPriviledgeEntityProvider() {
        super(PreacherPriviledgeImpl.class);
    }
}
