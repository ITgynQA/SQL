package ru.netology.web.test;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.open;


public class AccountLoginTest {

    public static QueryRunner runner = new QueryRunner();

    @SneakyThrows
    public static Connection getConn() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @BeforeEach
    @SneakyThrows
    public void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    @SneakyThrows
    public static void cleanDataBase() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM card_transactions");
        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    @Test
    void validLoginAccountTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void inValidUserTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.generateUser();
        loginPage.enter(authInfo);
        loginPage.errorMessageVisible();

    }

    @Test
    void inValidCodeTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.generateCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.errorMessageVisible();

    }

    @Test
    void inValidPasswordTest() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.generateInvalidUser();
        loginPage.enter(authInfo);
        DataHelper.AuthInfo authInfo1;
        authInfo1 = DataHelper.generateInvalidUser();
        loginPage.enter(authInfo1);
        DataHelper.AuthInfo authInfo2;
        authInfo2 = DataHelper.generateInvalidUser();
        loginPage.enter(authInfo2);
        var result = DataHelper.getBlockingUser();
        Assertions.assertEquals("blocked", result);
    }

}

