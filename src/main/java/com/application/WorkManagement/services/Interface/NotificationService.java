package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.responses.table.TableActivityResponse;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;

import java.util.List;

public interface NotificationService {

    List<TableActivityResponse> readNotifications(String accountId) throws DataNotFoundException;

}
