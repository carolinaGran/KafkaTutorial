package com.banking.account.query.infrastructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{

    //acceso a los objetos a la instancia de la bbdd
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        //crear objeto backaccount e insertarlo en la bbdd
        var bankAccount= BankAccount.builder()
        .id(event.getId())
        .accountHolder(event.getAccountHolder())
        .creationDate(event.getCreatedDate())
        .accountType(event.getAccountType())
        .balance(event.getOpeningBalance())
        .build();
        accountRepository.save(bankAccount);


    }

    @Override
    public void on(FundsDepositedEvent event) {
        var bankAccount=accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()){
            return;
        }
        //balance actual
        var currentBalance=bankAccount.get().getBalance();
        var latestBalance=currentBalance + event.getAmount();//balance final sumando el dinero introducido
        bankAccount.get().setBalance(latestBalance);//guardar valor nuevo
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        var bankAccount=accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()){
            return;
        }
        var currentBalance=bankAccount.get().getBalance();
        var latestBalance=currentBalance - event.getAmount();
        bankAccount.get().setBalance(latestBalance);
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());

    }
}
