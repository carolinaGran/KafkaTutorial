package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.domain.AggregateRoot;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import com.banking.cqrs.core.infraestructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
 @Autowired
 private EventStore eventStore;
    @Override
    public void save(AggregateRoot aggregate) {
        //getUncommitedChanges es para obtener los cambios que aun no han sido guardados
        eventStore.saveEvents(aggregate.getId(),aggregate.getUncommitedChanges(), aggregate.getVersion());
    aggregate.markChangesAsCommited();//para realizar una marca de que ya ha sido guardado
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate =new AccountAggregate();
        var events=eventStore.getEvent(id);//lista de eventos relacionada a este aggregate
        if(events!=null&&!events.isEmpty()){
            //el aggregate vuelve a ejecutar los eventos
            aggregate.replayEvents(events);
            var latestVersion =events.stream().map(x->x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());//le pasamos al aggregate la ultima version
        }
        return aggregate;
    }
}
