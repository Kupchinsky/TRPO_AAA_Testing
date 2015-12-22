package ru.killer666.aaa;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum RoleEnum {
    READ(1), WRITE(2), EXECUTE(4);

    @Getter
    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }
}