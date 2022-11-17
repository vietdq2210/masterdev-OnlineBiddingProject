package com.ghtk.onlinebiddingproject.models.requests;

import com.ghtk.onlinebiddingproject.models.dtos.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignup {

    @NotBlank(message = "Username không được để trống!")
    @Pattern(regexp = "[a-zA-Z0-9_.]*", message = "username không được chứa ký tự đặc biệt!")
    @Length(min = 5, max = 30, message = "username có độ dài từ 5 - 30 ký tự!")
    private String username;

    @NotBlank(message = "Password không được để trống!")
    @Length(min = 8, message = "password phải ít nhất 8 ký tự!")
    private String password;

    @NotBlank(message = "Tên người dùng không được để trống!")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "tên người dùng không được chứa ký tự đặc biệt hoặc số!")
    private String name;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "email không hợp lệ!")
    private String email;

    @NotNull(message = "Role người dùng không được để trống!")
    private RoleDto role;
}
