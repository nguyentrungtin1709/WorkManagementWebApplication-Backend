package com.application.WorkManagement.entities.CompositePrimaryKeys;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.TableEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class TableCompositeId implements Serializable {

    private Account account;

    private TableEntity table;

}
