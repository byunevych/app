package com.webapp.common.utils;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FakeUtil {
    @Getter
    private static Faker faker = new Faker();

    public static String generateHash() {
        return faker.regexify("[a-z0-9]{10}");
    }

}