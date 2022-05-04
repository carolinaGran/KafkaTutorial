package com.banking.account.query.domain;

import com.banking.account.common.dto.AccountType;
import com.banking.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data//declarar lombok
@NoArgsConstructor
@AllArgsConstructor
@Builder//simplifica la crecion de objetos
@Entity//para persistencia de datos
public class BankAccount extends BaseEntity {
    @Id
    private String id;//clave primaria de identidad de la cuenta
    private String accountHolder;//nombre del usuario que creo la cuenta
    private Date creationDate;
    private AccountType accountType;//tipo de cuenta
    private double balance;//primer ingreso
}
