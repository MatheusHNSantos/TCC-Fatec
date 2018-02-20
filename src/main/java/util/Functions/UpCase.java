package util.Functions;

public class UpCase {

    public static String upCaseFirst(String value) {

        if(value != null){
            String[] completeName = value.split(" ");
            String firstName = completeName[0];

            // Convert String to char array.
            char[] array = firstName.toCharArray();
            // Modify first element in array.
            array[0] = Character.toUpperCase(array[0]);
            // Return string.

            return new String(array);
        }else{
            return null;
        }
    }
}
