package brenda.com.showoff.Util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {


    private Pattern pattern;
    private Matcher matcher;

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

    public Validator(){
        pattern = Pattern.compile(USERNAME_PATTERN);
    }


    public boolean validate(final String username){

        matcher = pattern.matcher(username);
        return matcher.matches();

    }


    public static boolean isValidName(String name) {
        String NAME_PATTERN = "^[\\p{L} '-]+$";
        // String NAME_PATTERN = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email1) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email1);
        return matcher.matches();
    }

    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        return pass != null && pass.length() > 0;
    }

    // validating password with retype password
    public static boolean checkPassWordAndConfirmPassword(String pass, String cpass) {
        boolean pstatus = false;
        if (pass != null && cpass != null) {
            if (pass.equals(cpass)) {
                //editor.putString("password", cpass);
                pstatus = true;
            }
        }
        return pstatus;
    }

    public static boolean isValidMobile(String phone2) {
        return android.util.Patterns.PHONE.matcher(phone2).matches();
    }

}
