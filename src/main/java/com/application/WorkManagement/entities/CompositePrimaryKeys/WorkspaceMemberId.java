package com.application.WorkManagement.entities.CompositePrimaryKeys;


import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Workspace;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberId implements Serializable {

    private Account account;

    private Workspace workspace;

}
