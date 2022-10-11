package com.example.moneyapp.data;

import com.example.moneyapp.domain.entities.User;

import junit.framework.TestCase;

import org.junit.Test;

public class ValidatorTest extends TestCase {

    public void testMethodValidPhoneNumber() {
        assertTrue(Validator.methodValidPhoneNumber("+380-99-990-99-99"));

        assertFalse(Validator.methodValidPhoneNumber("+38099-990-99-99"));

        assertFalse(Validator.methodValidPhoneNumber("099-990-99-99"));

        assertFalse(Validator.methodValidPhoneNumber("+380999909999"));
    }

    public void testMethodValidEmail() {
        assertTrue(Validator.methodValidEmail("ivan@gmail.com"));

        assertFalse(Validator.methodValidEmail("ivangmail.com"));

        assertFalse(Validator.methodValidEmail("ivan@mail.ru"));

        assertFalse(Validator.methodValidEmail("ivan@yandex.ru"));

        assertFalse(Validator.methodValidEmail("ivan@yandex.ua"));

        assertFalse(Validator.methodValidEmail("ivan@rambler.ru"));
    }

    public void testMethodCheckHomeAddress() {
        assertTrue(Validator.methodCheckHomeAddress("Kyiv"));

        assertTrue(Validator.methodCheckHomeAddress("Kharkiv"));

        assertTrue(Validator.methodCheckHomeAddress("Lviv"));

        assertTrue(Validator.methodCheckHomeAddress("Dnipro"));

        assertTrue(Validator.methodCheckHomeAddress("Odesa"));

        assertFalse(Validator.methodCheckHomeAddress("Lyubotin"));
    }


    public static class ValidatorForPersonalityTest{
        @Test
        public void testMethodCheckPersonalityData(){
            assertTrue(Validator.ValidatorForPersonality.methodCheckPersonalityData("Ivan"));

            assertFalse(Validator.ValidatorForPersonality.methodCheckPersonalityData("IVAN"));

            assertFalse(Validator.ValidatorForPersonality.methodCheckPersonalityData("ivan"));

            assertFalse(Validator.ValidatorForPersonality.methodCheckPersonalityData("Іван"));
        }
    }


    public static class PasswordValidatorTest{

        @Test
        public void testMethodValidPassword(){

            User user = new User("Ivan", "Ivanchenko",
                    "+380-99-900-90-90", "colivan@gmail.com","1999-09-08", "MALE",
                    "Kharkiv", 0);

            assertTrue(Validator.PasswordValidator.methodValidPassword(user, "Zork-9988"));

            //length password long <20
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Kharkiv-Ukraine@1988-90-90"));
            //length password short >8
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Kha@12"));

            //few letter >3
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Ka@12"));

            //UpperCaseLetter
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "zeka-9090"));

            //Without one numbers
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Zeka-k"));

            //Without one symbol
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Zeka1234"));

            //with person name or surname
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Ivan-9988"));

            //with phone number
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Zork-0999009090"));

            //with email text
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "cOlivan@9988"));

            //with birthday
            assertFalse(Validator.PasswordValidator.methodValidPassword(user, "Zeka@00019990908"));
        }
    }

}