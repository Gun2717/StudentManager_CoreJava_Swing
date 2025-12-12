package fit.se.service;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean validateAge(int age) {
        return age >= 0;
    }

    public boolean validateScore(double score) {
        return score >= 0 && score <= 10;
    }

    public boolean validateID(String id) {
        return !id.isEmpty();
    }
}
