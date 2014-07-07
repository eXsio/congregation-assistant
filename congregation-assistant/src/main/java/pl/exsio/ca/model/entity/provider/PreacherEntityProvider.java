package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.PreacherImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class PreacherEntityProvider extends TransactionalEntityProvider {

    protected PreacherEntityProvider() {
        super(PreacherImpl.class);
    }
}
