package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.EventImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class EventEntityProvider extends TransactionalEntityProvider {

    protected EventEntityProvider() {
        super(EventImpl.class);
    }
}
