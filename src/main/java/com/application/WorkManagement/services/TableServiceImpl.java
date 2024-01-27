package com.application.WorkManagement.services;

import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableServiceImpl implements TableService {

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final TableRepository tableRepository;

    private final TableMemberRepository tableMemberRepository;

    private final TableStarRepository tableStarRepository;

    private final AccountRepository accountRepository;

    private final ActivityRepository activityRepository;

    @Autowired
    public TableServiceImpl(
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            TableRepository tableRepository,
            TableMemberRepository tableMemberRepository,
            TableStarRepository tableStarRepository,
            AccountRepository accountRepository,
            ActivityRepository activityRepository
    ) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.tableRepository = tableRepository;
        this.tableMemberRepository = tableMemberRepository;
        this.tableStarRepository = tableStarRepository;
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
    }


}
