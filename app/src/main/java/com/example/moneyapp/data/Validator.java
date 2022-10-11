package com.example.moneyapp.data;

import com.example.moneyapp.domain.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    //1номер телефона
    public static boolean methodValidPhoneNumber(String phoneNumber){
        //только в формате +380-XX-XXX-XX-XX +380 XX XXX XX XX
        String phoneRegex = "^\\+?3?8?(0[\\s\\.-]\\d{2}[\\s\\.-]\\d{3}[\\s\\.-]\\d{2}[\\s\\.-]\\d{2})$";

        Pattern pattern = Pattern.compile(phoneRegex);
        if (pattern.matcher(phoneNumber).matches()) {
            return true;
        }
        return false;


    }
    //2email
    public static boolean methodValidEmail(String emailAddress){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (pattern.matcher(emailAddress).matches()){
            if(emailAddress.contains("mail.ru") || emailAddress.contains("yandex") || emailAddress.contains("rambler")) return false;
            return true;
        }
        return false;
    }

    //3роверка домашнего адреса
    public static boolean methodCheckHomeAddress(String homeAddress){
        //TODO
        return homeAddress.contains("Kyiv") || homeAddress.contains("Kharkiv")
                || homeAddress.contains("Lviv") || homeAddress.contains("Dnipro")
                || homeAddress.contains("Odesa");
    }

    public static class ValidatorForPersonality{
        //4 пропервка ФИО на корректность
        public static boolean methodCheckPersonalityData(String text){
            //проверяем на кирилицу, большую первую букву, и маленькие осталные
            return isLatin(text) && Character.isUpperCase(text.charAt(0)) && isStringLowerCase(text.substring(1));
        }

        //4.1проверка на маленькие буквы
        private static boolean isStringLowerCase(String substring) {
            for(int i = 0; i < substring.length(); i++){
                if(!Character.isLowerCase(substring.charAt(i))) return false;
            }
            return true;
        }

        //проверка на английские буквы
        public static boolean isLatin(String s) {
            boolean result = true;
            for (char a : s.toCharArray()) {
                if (Character.UnicodeBlock.of(a) != Character.UnicodeBlock.BASIC_LATIN) {
                    result = false;
                    break;
                }
            }
            return result;
        }
    }
    //внутрений статический класс который нужно обьявить
    public static class PasswordValidator{
        public PasswordValidator(){}
        //подумать
        //3пароль проверяем
        public  static boolean methodValidPassword(User user, String password){
            //не менше 8 символов
            //только латиница
            //и верхний и нижний регист
            //обязательно: одна с верхним регистром, один символ, и одна цифра
            int letter = 0;
            int upperCase = 0;
            int numbers = 0;
            int symbols = 0;


            for(int i = 0; i < password.length(); i++){
                if(methodCheckTextStrings(String.valueOf(password.charAt(i)))) letter++;
                if(methodCheckTextNumbers(String.valueOf(password.charAt(i)))) numbers++;
                if(methodCheckTextSymbols(String.valueOf(password.charAt(i)))) symbols++;
                if(Character.isUpperCase(password.charAt(i))) upperCase++;
            }

            System.out.println("letter=" + letter + " upperCase=" + upperCase + " numbers=" + numbers + " symbols=" + symbols);

            boolean result = password.length() >= 8 && password.length() <= 20 && letter >= 3 && upperCase >= 1 &&
                    numbers >=1 && symbols >=1 && !isPasswordWithPersonalityData(user, password);

            //TODO проверка пароля на наличие в нем личных доступных даных: ФИО, дата рождения, телфеон, email.


            return result;
        }

        //3.1проверка на буквы
        private static boolean methodCheckTextStrings(String s) {
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            Matcher matcher = pattern.matcher(s);
            boolean result = matcher.find();

            return result;
        }


        //3.1проверка на числа
        private static boolean methodCheckTextNumbers(String s) {
            Pattern pattern = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
            Matcher matcher = pattern.matcher(s);
            boolean result = matcher.find();

            return result;
        }

        //3.2проверка на символы
        private static boolean methodCheckTextSymbols(String s) {
            String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";

            return specialCharactersString.contains(s);
        }

        //3.4 проверка пароля на наличие в нем личных доступных даных: ФИО, дата рождения, телфеон, email.
        private static boolean isPasswordWithPersonalityData(User user, String password) {
            //если есть личные даные true, в противном случае false


            //ищет отывок из email в пароле
            String email = user.getEmail().substring(0, user.getEmail().indexOf('@')).toLowerCase();
            //System.out.println("email=" + email);

            //уменшаем до маленькой буквы вся строку
            String passwordToLoweCase = password.toLowerCase();

            return passwordToLoweCase.contains(user.getFirstName().toLowerCase()) || passwordToLoweCase.contains(user.getLastName().toLowerCase())
                    || passwordToLoweCase.contains(email) || isPhoneNumberInPassword(password, user.getPhone())
                    || passwordCommonKeys(password) || isBirthDayInPassword(password, user.getBirthday());
        }


        //3.4.2 проверка на вписаный номер телефона пароле

        private static boolean isPhoneNumberInPassword(String password, String number){
            String phoneNumberText = returnNumberFromString(number);
            String numbersInPassword = returnNumberFromString(password);


            //TODO вернуть в условие
            return numbersInPassword.contains(phoneNumberText) || numbersInPassword.contains(phoneNumberText.substring(2));
        }

        //3.4.2.1 Возвращаем номер телфеона в String
        private static String returnNumberFromString(String text){

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < text.length(); i++){
                if(methodCheckTextNumbers(String.valueOf(text.charAt(i)))){
                    sb.append(Integer.parseInt(String.valueOf(text.charAt(i))));
                }
            }

            return sb.toString();
        }

        //проверка на распространеные ключи
        private static boolean passwordCommonKeys(String password) {
            String commonKeysValue[] = {
                    "password", "qwerty", "abc", "abcd"
            };

            for(String s : commonKeysValue){
                if(password.toLowerCase().contains(s)) return true;
            }

            return false;
        }

        //3.4.3
        private static boolean isBirthDayInPassword(String password, String birthday) {
            //TODO проверка на наличие даты рождения и названия месяца
            //String numbersInPasswordString = returnNumberFromString(password);
            String numbersInPasswordString = birthday.replace("-", "").replace(".", "");


            return password.contains(numbersInPasswordString);
        }

    }
}
