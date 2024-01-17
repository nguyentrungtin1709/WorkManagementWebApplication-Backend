package com.application.WorkManagement.entities.CompositePrimaryKeys;


import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Workspace;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class WorkspaceMemberId implements Serializable {

    private Account account;

    private Workspace workspace;

}
