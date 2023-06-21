package ru.netology.web.data;


import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.Locale;

import static ru.netology.web.test.AccountLoginTest.getConn;
import static ru.netology.web.test.AccountLoginTest.runner;

public class DataHelper {
    private static String balance;

    private DataHelper() {
    }

    private static Faker faker = new Faker(new Locale("en"));

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @SneakyThrows
    public static VerificationCode getVerificationCode() {
        var authCode = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var code = runner.query(conn, authCode, new ScalarHandler<String>());
        return new VerificationCode(code);
    }

    public static VerificationCode generateCode() {
        return new VerificationCode(faker.numerify("######"));
    }

    private static String generateLogin() {
        return faker.name().username();
    }

    private static String generatePassword() {
        return faker.internet().password();
    }

    public static AuthInfo generateUser() {
        return new AuthInfo(generateLogin(), generatePassword());
    }

    public static AuthInfo generateInvalidUser() {
        return new AuthInfo(getAuthInfo().getLogin(), generatePassword());
    }

    @SneakyThrows
    public static String getBlockingUser() {
        var blockingStatus = "SELECT status FROM users LIMIT 1";
        var conn = getConn();
        var result = runner.query(conn, blockingStatus, new ScalarHandler<String>());
        return result;

    }

}


