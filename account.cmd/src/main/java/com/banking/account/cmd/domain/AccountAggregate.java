package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
private Boolean active;//comprobar si la cuenta esta abierta
    //balance de la cuenta
    private double balance;
    //metodo publico para devolver balance a AccountCommandHandler
    public double getBalance(){
        return this.balance;
    }
    //cuando se llame al constructor se dara de alta la cuenta
    public AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())//id cuenta
                .accountHolder(command.getAccountHolder())//nombre cliente
                .createdDate(new Date())//fecha creacion
                .accountType(command.getAccountType())//tipo de cuenta
                .openingBalance(command.getOpeningBalance())//dinero ingresado a la hora de abrir la cuenta
                .build()
        );
    }
    //metodo para introducir datos obtenidos en las propiedades de la clase aggregate
    public void  apply(AccountOpenedEvent event){
        this.id=event.getId();
        this.active=true;
        this.balance=event.getOpeningBalance();
    }
    //funcion para realizar un deposito en la cuenta
    public void depositFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("los fondos no pueden ser depositados en esta cuenta");
        }
        if (amount <=0){
            throw new IllegalStateException("El deposito no puede ser igual o menor de 0");
        }
        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build()
        );

    }
    //apply para deposito de dinero,para actualizar los valores de la funcion de arriba
    public void apply(FundsDepositedEvent event){
        this.id=event.getId();
        this.balance+=event.getAmount();//suma cantidad nueva a la cantidad anterior de la cuenta
    }
    //retirar dinero de la cuenta
    public void withdrawFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("cuenta cerrada");
        }
        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build()
        );
    }
    public void apply(FundsWithdrawnEvent event){
        this.id=event.getId();
        this.balance-=event.getAmount();//suma cantidad nueva a la cantidad anterior de la cuenta
    }
    public void closeAccount(){
        if (!this.active){
            throw new IllegalStateException("cuenta cerrada");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build()
        );
    }
    public void apply(AccountClosedEvent event){
        this.id=event.getId();
        this.active=false;//suma cantidad nueva a la cantidad anterior de la cuenta
    }
}
