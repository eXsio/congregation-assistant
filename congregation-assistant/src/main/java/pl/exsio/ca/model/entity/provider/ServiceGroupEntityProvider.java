package pl.exsio.ca.model.entity.provider;

import pl.exsio.ca.model.entity.ServiceGroupImpl;
import pl.exsio.frameset.vaadin.entity.provider.TransactionalEntityProvider;

/**
 *
 * @author exsio
 */
public class ServiceGroupEntityProvider extends TransactionalEntityProvider {

    protected ServiceGroupEntityProvider() {
        super(ServiceGroupImpl.class);
    }
}
