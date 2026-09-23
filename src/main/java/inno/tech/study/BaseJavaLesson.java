package inno.tech.study;

import java.util.ArrayList;
import java.util.List;

public class BaseJavaLesson {
    public static boolean isEven(int n) {
        if (n % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String checkAccess(int age) {
        if (age > 18) {
            return "Allowed";
        } else {
            return "Denied";
        }
    }

    public static boolean isPositive(int n) {
        return (n >= 0) ? true : false;
    }

    public static String getGrade(int score) {
        if (score >= 0 & score <= 20) {
            return "E";
        } else if (score >= 21 & score <= 40) {
            return "D";
        } else if (score >= 41 & score <= 60) {
            return "C";
        } else if (score >= 61 & score <= 80) {
            return "B";
        } else if (score >= 81 & score <= 100) {
            return "A";
        } else {
            return "Error";
        }
    }

    public static String blastOff(int start) {
        String finalString = "";
        for (int i = start; i >= 0; i --) {
            if (i > 0) {
                finalString += i;
                finalString += " ";
            } else {
                finalString += "Let's go!";
            }

        }
        return finalString;
    }

    public static int sumToN(int n) {
        int sum = 0;
        for (int i = 0; i <= n; i ++) {
            sum += i;
        }
        return sum;
    }

    public static boolean hasBug(String[] messages) {
        for (String message: messages) {
            if (message == "Bug") {
                return true;
            }
        }
        return false;
    }

    public static String getEvenInRange(int start, int end) {
        String result = "";
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (i == start || i == start + 1) {
                    result += i;
                } else {
                    result = result + " " + i;
                }
            }
        }
        return result;
    }

    public static int findMax(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int result = arr[0];
        for (int num : arr) {
            if (num > result) {
                result = num;
            }
        }
        return result;
    }

    public static String[] reverse(String[] arr) {
        int len_arr = arr.length;
        String[] result = new String[len_arr];
        for (String i : arr) {
            len_arr -= 1;
            result[len_arr] = i;
        }
        return result;
    }

    public static int calcAverage(List<Integer> list) {
        int len_list = list.size();
        int sum_list = 0;
        for (Integer num : list) {
            sum_list += num;
        }
        return sum_list / len_list;
    }

    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>();
        for (String name : list) {
            if (!name.equals(nameToRemove)) {
                result.add(name);
            }
        }
        return result;
    }
}
