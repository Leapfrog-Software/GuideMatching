package leapfrog_inc.guidematching.Http.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import leapfrog_inc.guidematching.System.Base64Utility;
import leapfrog_inc.guidematching.System.DateUtility;

public class GuideData {

    public static class GuideScheduleData {

        public Date day;
        public  boolean[] isFreeList;

        public static GuideScheduleData create(String string) {

            GuideScheduleData scheduleData = new GuideScheduleData();

            String[] splited = string.split("_", -1);
            if (splited.length != 49) {
                return null;
            }
            scheduleData.day = DateUtility.stringToDate(splited[0], "yyyyMMdd");
            if (scheduleData.day == null) {
                return null;
            }

            scheduleData.isFreeList = new boolean[48];

            for (int i = 1; i < splited.length; i++) {
                if (splited[i].equals("1")) {
                    scheduleData.isFreeList[i - 1] = true;
                } else {
                    scheduleData.isFreeList[i - 1] = false;
                }
            }
            return scheduleData;
        }

        public String toString() {

            StringBuffer str = new StringBuffer();

            String dayStr = DateUtility.dateToString(day, "yyyyMMdd");
            str.append(dayStr);
            str.append("_");

            for (int i = 0; i < isFreeList.length; i++) {
                if (isFreeList[i]) str.append("1");
                else str.append("0");
                if (i != isFreeList.length - 1) {
                    str.append("_");
                }
            }

            return str.toString();
        }
    }

    public static class GuideTourData {

        public String id = "";
        public String name = "";
        public String area = "";
        public String description = "";
        public int fee = 0;
        public String highlights1Title = "";
        public String highlights1Body = "";
        public String highlights2Title = "";
        public String highlights2Body = "";
        public String highlights3Title = "";
        public String highlights3Body = "";
        public ArrayList<Date> days = new ArrayList<Date>();
        public int startTime = 0;
        public int endTime = 0;
        public String departurePoint = "";
        public String returnDetail = "";
        public String inclusions = "";
        public String exclusions = "";

        public static GuideTourData create(String string) {

            String[] splited = string.split("-", -1);
            if (splited.length == 18) {
                GuideTourData tourData = new GuideTourData();

                tourData.id = splited[0];
                tourData.name = Base64Utility.decode(splited[1]);
                tourData.area = Base64Utility.decode(splited[2]);
                tourData.description = Base64Utility.decode(splited[3]);
                tourData.fee = Integer.parseInt(splited[4]);
                tourData.highlights1Title = Base64Utility.decode(splited[5]);
                tourData.highlights1Body = Base64Utility.decode(splited[6]);
                tourData.highlights2Title = Base64Utility.decode(splited[7]);
                tourData.highlights2Body = Base64Utility.decode(splited[8]);
                tourData.highlights3Title = Base64Utility.decode(splited[9]);
                tourData.highlights3Body = Base64Utility.decode(splited[10]);

                String[] daysStrs = splited[11].split("\\|");
                for (int i = 0; i < daysStrs.length; i++) {
                    Date date = DateUtility.stringToDate(daysStrs[i], "yyyyMMdd");
                    if (date != null) {
                        tourData.days.add(date);
                    }
                }

                tourData.startTime = Integer.parseInt(splited[12]);
                tourData.endTime = Integer.parseInt(splited[13]);
                tourData.departurePoint = Base64Utility.decode(splited[14]);
                tourData.returnDetail = Base64Utility.decode(splited[15]);
                tourData.inclusions = Base64Utility.decode(splited[16]);
                tourData.exclusions = Base64Utility.decode(splited[17]);
                return tourData;
            }
            return null;
        }

        public String toString() {

            String id = this.id;
            String name = Base64Utility.encode(this.name);
            String area = Base64Utility.encode(this.area);
            String fee = String.valueOf(this.fee);
            String description = Base64Utility.encode(this.description);
            String highlights1Title = Base64Utility.encode(this.highlights1Title);
            String highlights1Body = Base64Utility.encode(this.highlights1Body);
            String highlights2Title = Base64Utility.encode(this.highlights2Title);
            String highlights2Body = Base64Utility.encode(this.highlights2Body);
            String highlights3Title = Base64Utility.encode(this.highlights3Title);
            String highlights3Body = Base64Utility.encode(this.highlights3Body);

            StringBuffer daysBuffer = new StringBuffer();
            for (int i = 0; i < this.days.size(); i++) {
                if (i > 0) {
                    daysBuffer.append("|");
                }
                String dateStr = DateUtility.dateToString(this.days.get(i), "yyyyMMdd");
                daysBuffer.append(dateStr);
            }
            String days = daysBuffer.toString();

            String startTime = String.valueOf(this.startTime);
            String endTime = String.valueOf(this.endTime);
            String departurePoint = Base64Utility.encode(this.departurePoint);
            String returnDetail = Base64Utility.encode(this.returnDetail);
            String inclusions = Base64Utility.encode(this.inclusions);
            String exclusions = Base64Utility.encode(this.exclusions);

            return id + "-"
                    + name + "-"
                    + area + "-"
                    + description + "-"
                    + fee + "-"
                    + highlights1Title + "-"
                    + highlights1Body + "-"
                    + highlights2Title + "-"
                    + highlights2Body + "-"
                    + highlights3Title + "-"
                    + highlights3Body + "-"
                    + days + "-"
                    + startTime + "-"
                    + endTime + "-"
                    + departurePoint + "-"
                    + returnDetail + "-"
                    + inclusions + "-"
                    + exclusions;
        }
    }

    public static class GuideBankAccountData {

        public String name = "";
        public String kana = "";
        public String bankName = "";
        public String bankBranchName = "";
        public String accountType = "";
        public String accountNumber = "";

        public static GuideBankAccountData create(String string) {

            GuideBankAccountData account = new GuideBankAccountData();

            String decode = Base64Utility.decode(string);
            String[] splited = decode.split(",", -1);
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
    public String area;
    public String keyword;
    public String category;
    public String message;
    public int applicableNumber;
    public int fee;
    public String notes;
    public ArrayList<GuideScheduleData> schedules;
    public ArrayList<GuideTourData> tours;
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
            guideData.area = Base64Utility.decode(json.getString("area"));
            guideData.keyword = Base64Utility.decode(json.getString("keyword"));
            guideData.category = Base64Utility.decode(json.getString("category"));
            guideData.message = Base64Utility.decode(json.getString("message"));
            guideData.applicableNumber = Integer.parseInt(json.getString("applicableNumber"));
            guideData.fee = Integer.parseInt(json.getString("fee"));
            guideData.notes = Base64Utility.decode(json.getString("notes"));

            guideData.schedules = new ArrayList<GuideScheduleData>();
            String[] scheduleStrs = json.getString("schedules").split("/");
            for (String schedule : scheduleStrs) {
                GuideScheduleData scheduleData = GuideScheduleData.create(schedule);
                if (scheduleData != null) {
                    guideData.schedules.add(scheduleData);
                }
            }

            guideData.tours = new ArrayList<GuideTourData>();
            String[] toursStrs = json.getString("tours").split("/");
            for (String tour : toursStrs) {
                GuideTourData tourData = GuideTourData.create(tour);
                if (tourData != null) {
                    guideData.tours.add(tourData);
                }
            }

            guideData.loginDate = DateUtility.stringToDate(json.getString("loginDate"), "yyyyMMddkkmmss");
            if (guideData.loginDate == null) {
                return null;
            }

            guideData.stripeAccountId = json.getString("stripeAccountId");

            String bankAccountStr = json.getString("bankAccount");
            guideData.bankAccount = GuideBankAccountData.create(bankAccountStr);

            return guideData;
        } catch(Exception e) {}

        return null;
    }
}
