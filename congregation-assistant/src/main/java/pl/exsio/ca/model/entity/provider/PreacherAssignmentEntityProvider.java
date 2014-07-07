package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.PreacherAssignmentImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class PreacherAssignmentEntityProvider extends TransactionalEntityProvider {

    protected PreacherAssignmentEntityProvider() {
        super(PreacherAssignmentImpl.class);
    }
}
