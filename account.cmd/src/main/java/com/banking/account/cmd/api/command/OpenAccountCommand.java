package com.banking.account.cmd.api.command;

import com.banking.account.common.dto.AccountType;
import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    //name of client
    private String accountHolder;
    //type of count
    private AccountType accountType;
    //money with open count
    private double openingBalance;
}
