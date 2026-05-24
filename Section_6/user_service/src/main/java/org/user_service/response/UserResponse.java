package org.user_service.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Данные пользователя")
public class UserResponse {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private long id;

    @Schema(description = "Имя пользователя", example = "Jon")
    private String name;

    @Schema(description = "Email пользователя", example = "jon@mail.ru")
    private String email;

    @Schema(description = "Возраст пользователя", example = "30")
    private int age;

    @Schema(description = "Дата и время создания записи")
    private LocalDateTime created_at;

    public UserResponse(long id, String name, String email, int age, LocalDateTime created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
