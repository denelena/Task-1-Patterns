package ru.netology;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserDataGenerator {
    private UserDataGenerator(){}

    public static UserDataEntry createUserDataEntry(){
        Faker fkr = new Faker(new Locale("ru"));

        String fullName = fkr.name().fullName();
        //методом научного тыка было найдено, что авторы app-card-delivery.jar не считают ё русской буквой.
        // Кот Васильевич Ёжкин негодует! LMAO+ROTFL+STC
        fullName = fullName.replace('ё', 'е').replace('Ё', 'Е');

        String phone = fkr.phoneNumber().phoneNumber().replace("(","").replace(")","").replace("-","");

        //city - we pick one randomly out of these 4 (ибо нет гарантии, что Faker даст разрешенный город):
        String[] availableCities = new String[]{"Казань", "Москва", "Санкт-Петербург", "Новосибирск"};
        int nearlyRandomSelector = LocalTime.now().getSecond() % 4;
        String city = availableCities[nearlyRandomSelector];

        String firstDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String secondDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return new UserDataEntry(city, fullName, phone, firstDate, secondDate);
    }
}
