package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TestCardDelivery {

    private UserDataEntry userData;

    @BeforeEach
    void setUpTest() {
        //make random user
        userData = UserDataGenerator.createUserDataEntry();
        String initialRequestDate = generateDate(4);

        fillAndSubmitOrderForm(userData.getCity(), userData.getFullName(), userData.getPhoneNum(), initialRequestDate);
        //ожидаем первоначальный ответ для уникальной заявки
        waitForPopup("success-notification", "Успешно!", "Встреча успешно запланирована на " + initialRequestDate);
    }


    @Test
    void shouldRescheduleCardDelivery() {

        String rescheduleDate = generateDate(7);
        fillAndSubmitOrderForm(userData.getCity(), userData.getFullName(), userData.getPhoneNum(), rescheduleDate);
        //ожидаем popup с предложением перепланировать
        waitForPopup("replan-notification", "Необходимо подтверждение", "У вас уже запланирована встреча на другую дату. Перепланировать?");

        $$("[data-test-id='replan-notification'] button").find(exactText("Перепланировать")).click();

        waitForPopup("success-notification", "Успешно!", "Встреча успешно запланирована на " + rescheduleDate);
    }

    private String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private void fillAndSubmitOrderForm(String city, String userName, String phone, String appopintmentDate) {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue(city);

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(appopintmentDate);

        $("[data-test-id='name'] input").setValue(userName);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] [role='presentation']").click();

        $$(".button_theme_alfa-on-white").find(exactText("Запланировать")).click();
    }

    private void waitForPopup(String parentDataTestID, String popupTitle, String popupContent) {
        String titleCSS = "[data-test-id='" + parentDataTestID + "'] .notification__title";
        String contentCSS = "[data-test-id='" + parentDataTestID + "'] .notification__content";

        $(titleCSS).shouldBe(visible, Duration.ofSeconds(15));
        $(titleCSS).shouldHave(Condition.text(popupTitle), Duration.ofSeconds(15));
        $(contentCSS).shouldHave(Condition.text(popupContent), Duration.ofSeconds(15));
    }

}
