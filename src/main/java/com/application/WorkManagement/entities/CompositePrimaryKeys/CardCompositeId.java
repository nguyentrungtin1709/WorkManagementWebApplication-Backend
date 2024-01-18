package com.application.WorkManagement.entities.CompositePrimaryKeys;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Card;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CardCompositeId implements Serializable {

    private Account account;

    private Card card;

}
