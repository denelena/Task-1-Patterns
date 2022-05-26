package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

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
    }


    @Test
    void shouldRescheduleCardDelivery() {

        open("http://localhost:9999");

        //make initial order
        fillAndSubmitOrderForm(userData.getCity(), userData.getFullName(), userData.getPhoneNum(), userData.getInitialDate());
        //ожидаем подтверждение первого заказа на изначальную дату
        waitForPopup("success-notification", "Успешно!", "Встреча успешно запланирована на " + userData.getInitialDate());

        //второй заказ, с другой датой
        fillAndSubmitOrderForm(userData.getCity(), userData.getFullName(), userData.getPhoneNum(), userData.getRescheduledDate());
        //ожидаем popup с предложением перепланировать
        waitForPopup("replan-notification", "Необходимо подтверждение", "У вас уже запланирована встреча на другую дату. Перепланировать?");

        $$("[data-test-id='replan-notification'] button").find(exactText("Перепланировать")).click();
        //ожидаем подтверждение на перенесенную дату
        waitForPopup("success-notification", "Успешно!", "Встреча успешно запланирована на " + userData.getRescheduledDate());
    }

    private void fillAndSubmitOrderForm(String city, String userName, String phone, String appopintmentDate) {

        $("[data-test-id='city'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='city'] input").setValue(city);

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(appopintmentDate);

        $("[data-test-id='name'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue(userName);

        $("[data-test-id='phone'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='phone'] input").setValue(phone);

        WebElement checkBoxElement = $("[data-test-id='agreement'] .checkbox__control").toWebElement();
        if(!checkBoxElement.isSelected())
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
