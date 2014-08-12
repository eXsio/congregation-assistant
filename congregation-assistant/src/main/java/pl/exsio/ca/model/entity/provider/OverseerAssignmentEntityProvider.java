package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.OverseerAssignmentImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class OverseerAssignmentEntityProvider extends TransactionalEntityProvider {

    protected OverseerAssignmentEntityProvider() {
        super(OverseerAssignmentImpl.class);
    }
}
