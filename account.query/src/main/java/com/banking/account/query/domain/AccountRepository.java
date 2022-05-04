package com.banking.account.query.domain;

import com.banking.cqrs.core.domain.BaseEntity;
import com.banking.cqrs.core.events.BaseEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//permite dar mantenimiento a una entidad determinada en este caso<objeto BankAccount,tipo String>
public interface AccountRepository extends CrudRepository <BankAccount,String>{
    //al gestionar ahora en una bbdd Mysql solo se permite letura
    //busque una cuenta de banco cuando busque un accountholder (nombre del usuario de la cuenta)

    Optional<BankAccount> findByAccountHolder(String accountHolder) ;
    //guarde las cuentas que tengan un balance mayor que un determinado numero
    List<BaseEntity> findByBalanceGreaterThan(double balance);
    //guarde las cuentas que tengan un balance menor que un determinado numero
    List<BaseEntity> findByBalanceLessThan(double balance);


}
