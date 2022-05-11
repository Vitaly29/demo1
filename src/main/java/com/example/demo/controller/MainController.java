package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.Phone;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    // Вводится (inject) из application.properties.
    @Value("${welcome.message}")
    private String message;


    @Autowired
    JdbcTemplate jdbcTemplate;


    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        List<Phone> lp = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT id, fio, num FROM phone",
                (rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("fio"), rs.getString("num"))
        ).forEach(phone -> lp.add(phone));

        model.addAttribute("message", message);
        model.addAttribute("phones", lp);

        return "index";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name = "id", required = true) String id, Model model) {
        log.info("Delete id=" + id);
        jdbcTemplate.update("DELETE FROM phone WHERE id = ?", id);
        return "redirect:/index";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String delete(@RequestParam(name = "fio", required = true) String fio, @RequestParam(name = "num", required = true) String num, Model model) {
        log.info(String.format("Добавляем запись: %s %s", fio, num));
        jdbcTemplate.update("INSERT INTO phone(fio, num) VALUES(?,?)", fio, num);
        return "redirect:/index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String delete(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "fio", required = true) String fio, @RequestParam(name = "num", required = true) String num, Model model) {
        log.info(String.format("Редактируем запись: %s %s %s", id, fio, num));
        jdbcTemplate.update("UPDATE phone SET fio=?, num=? WHERE id=?", fio, num, id);
        return "redirect:/index";
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String createTable() {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE phone IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE phone(" +
                "id SERIAL, fio VARCHAR(255), num VARCHAR(255))");


        /*
         * Arrays.asList - Создаем List из массива строк
         * .stream() - Создаем из List поток (stream), можно сказать конвеер
         * .map(inStr -> inStr.split(" ") - перобразуем каждую входную строку в массив строк, разделитель пробел
         * .collect(Collectors.toList() - снова собираем все оъекты с конвеера в List и присваиваем переменной List<Object[]> splitUpNames
         */
        List<Object[]> splitUpNames = Arrays.asList("Флока 1111", "Иванов 222", "Петров 333", "Сидоров 4444").stream()
                .map(inStr -> inStr.split(" "))
                .collect(Collectors.toList());

        /*
         * Распечатываем каждый объект из List splitUpNames
         */
        splitUpNames.forEach(inStr -> log.info(String.format("Добавлена запись: %s %s", inStr[0], inStr[1])));


        /*
         * Используем пакетное выполнение INSERT для каждого элемента LIst
         */
        jdbcTemplate.batchUpdate("INSERT INTO phone(fio, num) VALUES (?,?)", splitUpNames);

        /*
         * Можно вставлять записи и так
         */
        jdbcTemplate.execute("INSERT INTO phone(fio, num) VALUES ('Флока1','1111-1')");
        jdbcTemplate.execute("INSERT INTO phone(fio, num) VALUES ('Иванов1','222-1')");

        log.info("Запрос записи, where fio = 'Флока':");
        jdbcTemplate.query(
                "SELECT id, fio, num FROM phone WHERE fio = ?", new Object[]{"Флока"},
                (rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("fio"), rs.getString("num"))
        ).forEach(phone -> log.info(phone.toString()));

        log.info("Запрос записи, where fio like 'Флока%':");
        jdbcTemplate.query(
                "SELECT id, fio, num FROM phone WHERE fio like ?", new Object[]{"Флока%"},
                (rs, rowNum) -> new Phone(rs.getLong("id"), rs.getString("fio"), rs.getString("num"))
        ).forEach(phone -> log.info(phone.toString()));

        return "create";
    }


}
