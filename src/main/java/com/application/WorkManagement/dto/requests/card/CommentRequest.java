package com.application.WorkManagement.dto.requests.card;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "Bình luận không được để trống")
    @Length(max = 1000, message = "Bình luận không được vượt quá 1000 ký tự")
    private String comment;

}
