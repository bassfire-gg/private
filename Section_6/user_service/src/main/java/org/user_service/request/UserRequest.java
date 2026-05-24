package org.user_service.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание или обновление пользователя")
public class UserRequest {

    @Schema(description = "Имя пользователя", example = "Kate", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Email пользователя", example = "kate@mail.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Возраст пользователя", example = "18", requiredMode = Schema.RequiredMode.REQUIRED)
    private int age;

    public UserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}