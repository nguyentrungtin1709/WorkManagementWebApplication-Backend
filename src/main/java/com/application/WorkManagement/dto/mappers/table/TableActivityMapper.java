package com.application.WorkManagement.dto.mappers.table;

import com.application.WorkManagement.dto.responses.table.TableActivityResponse;
import com.application.WorkManagement.dto.responses.table.activity.AccountActivity;
import com.application.WorkManagement.dto.responses.table.activity.CardActivity;
import com.application.WorkManagement.dto.responses.table.activity.CategoryActivity;
import com.application.WorkManagement.dto.responses.table.activity.TableActivity;
import com.application.WorkManagement.entities.Activity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TableActivityMapper implements Function<Activity, TableActivityResponse> {

    @Override
    public TableActivityResponse apply(Activity activity) {
        TableActivityResponse activityResponse = TableActivityResponse.builder().build();
        activityResponse.setId(
                activity.getUuid()
        );
        activityResponse.setAccount(
                AccountActivity
                        .builder()
                        .id(activity.getAccount().getUuid())
                        .name(activity.getAccount().getName())
                        .email(activity.getAccount().getEmail())
                        .avatar(activity.getAccount().getAvatar())
                        .build()
        );
        activityResponse.setType(
                activity.getActivityType()
        );
        activityResponse.setTable(
                TableActivity
                        .builder()
                        .id(activity.getTable().getUuid())
                        .name(activity.getTable().getName())
                        .build()
        );
        activityResponse.setCreatedAt(
                activity.getCreatedAt()
        );
        if (activity.getCategory() == null){
            activityResponse.setCategory(null);
        } else {
            activityResponse.setCategory(
                    CategoryActivity
                            .builder()
                            .id(activity.getCategory().getUuid())
                            .name(activity.getCategory().getName())
                            .build()
            );
        }
        if (activity.getCard() == null){
            activityResponse.setCard(null);
        } else {
            activityResponse.setCard(
                    CardActivity
                            .builder()
                            .id(activity.getCard().getUuid())
                            .name(activity.getCard().getName())
                            .build()
            );
        }
        String message = switch (activity.getActivityType()) {
            case CHANGE_SCOPE_TABLE -> {
                yield String.format("%s đã thay đổi phạm vi của bảng", activity.getAccount().getName());
            }
            case DELETE_CATEGORY ->  {
                yield String.format("%s đã xóa một danh mục trong bảng", activity.getAccount().getName());
            }
            case CREATE_CATEGORY -> {
                yield String.format("%s đã tạo danh mục %s trong bảng", activity.getAccount().getName(), activity.getCategory().getName());
            }
            case DELETE_CARD -> {
                yield String.format("%s đã xóa một thẻ trong danh mục %s", activity.getAccount().getName(), activity.getCategory().getName());
            }
            case CREATE_CARD -> {
                yield String.format("%s đã tạo thẻ %s trong danh mục %s", activity.getAccount().getName(), activity.getCard().getName(), activity.getCategory().getName());
            }
            case SET_DEADLINE -> {
                yield String.format("%s đã đặt ngày hết hạn cho thẻ %s trong danh mục %s", activity.getAccount().getName(), activity.getCard().getName(), activity.getCategory().getName());
            }
            case CHANGE_DEADLINE -> {
                yield String.format("%s đã cập nhật ngày hết hạn cho thẻ %s trong danh mục %s", activity.getAccount().getName(), activity.getCard().getName(), activity.getCategory().getName());
            }
            case DELETE_DEADLINE -> {
                yield String.format("%s đã xóa ngày hết hạn cho thẻ %s trong danh mục %s", activity.getAccount().getName(), activity.getCard().getName(), activity.getCategory().getName());
            }
            case REMINDER_DEADLINE -> {
                yield String.format("Thẻ %s trong danh mục %s sắp hết hạn", activity.getCard().getName(), activity.getCategory().getName());
            }
        };
        activityResponse.setMessage(message);
        return activityResponse;
    }

}
