package com.itmo.utils;

import com.itmo.collection.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Scanner;

/**
 * class for inputting element fields.
 */
public class FieldsScanner {
    private static Scanner sc;
    private static FieldsScanner fs;
    /**
     * Instantiates a new Input helper.
     *
     * @param scanner the scanner
     */

    private FieldsScanner(Scanner scanner){
        sc = scanner;
    }

    public void configureScanner(Scanner scanner){
        sc = scanner;
    }
    public static FieldsScanner getInstance(){
        if(fs==null) {
            fs = new FieldsScanner(new Scanner(System.in));
        }
        return fs;
    }


    /**
     * @param in  what to type?
     * @return user-entered string (may be empty)
     */
    public String scanLine(String in){
        System.out.println("Input " + in);
        return sc.nextLine().trim();
    }

    public String scanLine(){
        return sc.nextLine().trim();
    }

    /**
     * PRIVATE NON-BLANK LINE METHOD
     * @param in  what to type?
     * @return user-entered string (not empty)
     */
    private String scanNotEmptyLine(String in){
        String res = scanLine(in);
        while(res.trim().isEmpty()) {
            System.out.println("The string must not be empty or contain only spaces.");
            System.out.println("Input " + in + " again.");
            res = sc.nextLine();
        }
        return res.trim();
    }

    /**
     * method for entering string arguments. All String arguments in a lab cannot be empty.
     *
     *
     * @param in what to type?
     * @return in by user
     */
    public String scanStringNotEmpty(String in){
        String str = scanLine(in);
        while(str==null || str.equals("")|| str.isEmpty()|| str.trim().isEmpty()||str.trim().equals("")){
            System.out.println("Cannot be empty. Input " + in);
            str = sc.nextLine();
        }
        return str.trim();
    }



    public boolean scanYN(){
        String ans = FieldsScanner.getInstance().scanStringNotEmpty("y/n");
        while (true) {
            switch (ans) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("Input y/n");
            }
        }
    }


    /**
     * method to scan any Enum. checks if the string entered
     * by the user is an enum element that is passed in the second argument.
     *
     * @param canBeNull can Enum be empty?
     * @param enumType  enum type
     * @return enum
     */
    public Enum<?> scanEnum(boolean canBeNull, Class<? extends Enum> enumType){
        while(true) {
            String str = scanLine();
            try {
                if (str.equals("") && canBeNull) return null;
                else if (str.equals("")){
                    throw new NullPointerException();
                }
                return Enum.valueOf(enumType, str);
            } catch (IllegalArgumentException | NullPointerException e) {
                System.out.println("Please enter one of the enum values.");
            }
        }
    }

    /**
     * method to scan for an argument that must be an integer
     *
     * @param whatToEnter    what to enter?
     * @param positiveOnly should the number only be positive?
     * @return number int
     */
    public int scanInteger(String whatToEnter, boolean positiveOnly){
        while(true) {
            String input = scanNotEmptyLine(whatToEnter);
            int res;
            try{
                res = Integer.parseInt(input);
                if(positiveOnly && (res<=0)){
                    System.out.println("you must enter a number greater than zero");
                }else   {
                    return res;
                }
            }catch (Exception e){
                System.out.println("enter an integer");
            }
        }
    }

    /**
     * method to scan for an argument that must be float
     *
     * @param whatToEnter    what to enter?
     * @param positiveOnly should the number only be positive?
     * @return number float
     */
    public float scanFloat(String whatToEnter, boolean positiveOnly){
        while(true) {
            String input = scanNotEmptyLine(whatToEnter);
            float res;
            try{
                res = Float.parseFloat(input);
                if(positiveOnly && (res<=0)){
                    System.out.println("you must enter a number greater than zero");
                }else{
                    return res;
                }
            }catch (Exception e){
                System.out.println("enter a number");
            }
        }
    }

    /**
     * method to scan for an argument that must be long
     *
     * @param whatToEnter    what to enter?
     * @param positiveOnly should the number only be positive?
     * @return number long
     */
    public long scanLong(String whatToEnter, boolean positiveOnly){
        while(true) {
            String input = scanNotEmptyLine(whatToEnter);
            long res;
            try{
                res = Long.parseLong(input);
                if(positiveOnly && (res<=0)){
                    System.out.println("you must enter a number greater than zero");
                }else{
                    return res;
                }
            }catch (Exception e){
                System.out.println("enter a number");
            }
        }
    }


    /**
     * Scan local date time no null local date time.
     *
     * @param whatToEnter what to enter
     * @return the local date time
     */
    public LocalDateTime scanLocalDateTimeNoNull(String whatToEnter){
        System.out.println(whatToEnter);
        int god = scanInteger("year", true);
        System.out.println("Enter the month\nAvailable Month Values: ");
        Month mon;
        Arrays.stream(Month.values()).forEach(m -> {System.out.print(m + " ");});
        System.out.println();
        while(true){
            try{
                mon = (Month) scanEnum(false, Month.class);
                int day = scanInteger("number", true);
                return LocalDateTime.of(god, mon, day, 0, 0);
            }catch(IllegalArgumentException|NullPointerException e){
                System.out.println("enter the name of the month correctly");
            }catch (DateTimeException e){
                System.out.println("The number is not right for the month. again");
            }
        }
    }

    /**
     * Scan location location.
     *
     * @param whatToEnter what to enter
     * @return the location
     */
    public Location scanLocation(String whatToEnter){
        System.out.println("enter " + whatToEnter);
        int x = scanInteger("X coordinate of the place", false);
        long y = scanLong("Y coordinate of the place", false);
        float z = scanFloat("Z coordinate of the place", false);
        String locName = scanStringNotEmpty("place name");
        return new Location(x, y, z, locName);
    }

    /**
     * scans the whole labwork. checks if the fields are entered correctly. takes into account which fields
     * can be null, and which number fields are greater than zero
     *
     * @return labwork LabWork
     */
    public LabWork scanLabWork(){
        String name = scanStringNotEmpty("LabWork name");
        System.out.println("Coordinates.");
        int x = scanInteger("Х", false);
        int y = scanInteger("Y", false);
        Coordinates coordinates = new Coordinates(x, y);
        int weight = scanInteger("weight of LabWork", true);
        float minimalPoint = scanFloat("minimal point", true);
        System.out.println("Enter the haircolor of labwork. Available types: ");
        for(HairColor t : HairColor.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        HairColor type = (HairColor) scanEnum( true, HairColor.class);
        System.out.println("Enter the difficulty of the labwork. Available types: ");
        for(Difficulty t : Difficulty.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        Difficulty character = (Difficulty) scanEnum(false, Difficulty.class);
        Person killer = scanPerson("LabWork's author");
        return new LabWork(name, coordinates, weight,
                minimalPoint, type, character, killer);
    }

    /**
     * scans the entire Person. checks if the fields are entered correctly. takes into account which fields
     * can be null, and which number fields are greater than zero
     *
     * @param whatToEnter what to enter
     * @return person person
     */
    public Person scanPerson(String whatToEnter){
        System.out.println("enter " + whatToEnter);
        String name = scanLine("name");
        if(name.equals("")) return null; // Person может быть пустым
        LocalDateTime birthday = scanLocalDateTimeNoNull("date of birth");
        System.out.println("Enter hair color. Available types: ");
        for(Color t: Color.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        Color hairColor = (Color) scanEnum( true, Color.class);
        System.out.println("Enter nationality. Available types: ");
        for(Country t : Country.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        Country country = (Country) scanEnum( true, Country.class);
        Location loc = scanLocation("location");
        return new Person(name, birthday, hairColor, country, loc);
    }
}