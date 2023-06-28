package ru.netology.web.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.cleanDataBase;


public class AccountLoginTest {

    @BeforeEach
    @SneakyThrows
    public void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    @SneakyThrows
    public static void cleanDB() {
        cleanDataBase();
    }

    @SneakyThrows
    @Test
    void validUserTest() {
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
    void inValidVerificationCodeTest() {
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

        var authInfo1 = DataHelper.generateInvalidUser();
        loginPage.enter(authInfo1);
        loginPage.clearFields();

        var authInfo2 = DataHelper.generateInvalidUser();
        loginPage.enter(authInfo2);
        loginPage.clearFields();

        var authInfo3 = DataHelper.generateInvalidUser();
        loginPage.enter(authInfo3);
        loginPage.clearFields();

        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);

        var result = DataHelper.getBlockingUser();
        Assertions.assertEquals("blocked", result);
    }

}

