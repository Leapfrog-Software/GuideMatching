package leapfrog_inc.guidematching.Http.DataModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import leapfrog_inc.guidematching.System.Base64Utility;

public class GuideData {

    public static class GuideScheduleData {

        public Date day;
        public  boolean[] isFreeList;

        public static GuideScheduleData create(String string) {

            GuideScheduleData scheduleData = new GuideScheduleData();

            String[] splited = string.split("_");
            if (splited.length != 49) {
                return null;
            }
            String dayString = splited[0];
            if (dayString.length() != 8) {
                return null;
            }
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
                scheduleData.day = format.parse(dayString);
            } catch (Exception e) {
                return null;
            }

            scheduleData.isFreeList = new boolean[48];

            for (int i = 1; i < splited.length; i++) {
                if (splited[i].equals("1")) {
                    scheduleData.isFreeList[i] = true;
                } else {
                    scheduleData.isFreeList[i] = false;
                }
            }
            return scheduleData;
        }

        public String toString() {

            StringBuffer str = new StringBuffer();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String dayStr = format.format(day);
            str.append(dayStr);
            str.append("_");

            for (int i = 0; i < isFreeList.length; i++) {
                if (isFreeList[i]) str.append("1");
                else str.append("0");
                str.append("_");
            }

            return str.toString();
        }
    }

    public static  class GuideBankAccountData {

        public String name = "";
        public String kana = "";
        public String bankName = "";
        public String bankBranchName = "";
        public String accountType = "";
        public String accountNumber = "";

        public static GuideBankAccountData create(String string) {

            GuideBankAccountData account = new GuideBankAccountData();

            String decode = Base64Utility.decode(string);
            String[] splited = decode.split(",");
            if (splited.length != 6) {
                return account;
            }

            account.name = splited[0];
            account.kana = splited[1];
            account.bankName = splited[2];
            account.bankBranchName = splited[3];
            account.accountType = splited[4];
            account.accountNumber = splited[5];

            return account;
        }

        public String toString() {
            String str = name + "," + kana + "," + bankName + "," + bankBranchName + "," + accountType + "," + accountNumber;
            return Base64Utility.encode(str);
        }
    }

    public String id;
    public String email;
    public String name;
    public String nationality;
    public String language;
    public String specialty;
    public String category;
    public String message;
    public String timeZone;
    public int applicableNumber;
    public int fee;
    public String notes;
    public ArrayList<GuideScheduleData> schedules;
    public Date loginDate;
    public String stripeAccountId;
    public GuideBankAccountData bankAccount;

    static public GuideData create(JSONObject json) {

        try {
            GuideData guideData = new GuideData();
            guideData.id = json.getString("id");
            guideData.email = json.getString("email");
            guideData.name = Base64Utility.decode(json.getString("name"));
            guideData.nationality = Base64Utility.decode(json.getString("nationality"));
            guideData.language = Base64Utility.decode(json.getString("language"));
            guideData.specialty = Base64Utility.decode(json.getString("specialty"));
            guideData.category = Base64Utility.decode(json.getString("category"));
            guideData.message = Base64Utility.decode(json.getString("message"));
            guideData.timeZone = Base64Utility.decode(json.getString("timeZone"));
            guideData.applicableNumber = Integer.parseInt(json.getString("applicableNumber"));
            guideData.fee = Integer.parseInt(json.getString("fee"));
            guideData.notes = Base64Utility.decode(json.getString("notes"));

            String[] scheduleStrs = json.getString("schedules").split("/");
            for (String schedule : scheduleStrs) {
                GuideScheduleData scheduleData = GuideScheduleData.create(schedule);
                if (scheduleData != null) {
                    guideData.schedules.add(scheduleData);
                }
            }

            String loginDateStr = json.getString("loginDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddkkmmss");
            format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
            guideData.loginDate = format.parse(loginDateStr);

            guideData.stripeAccountId = json.getString("stripeAccountId");

            String bankAccountStr = json.getString("bankAccount");
            guideData.bankAccount = GuideBankAccountData.create(bankAccountStr);

            return guideData;
        } catch(Exception e) {}

        return null;
    }
}
