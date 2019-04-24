package invest.com.swapp.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by loey on 7/2/17.
 */

public class UtilityHelper {

    /**
     *  Utility Helper for Directory / Date / Time
     *
     * */

    private static final UtilityHelper ourInstance = new UtilityHelper();

    public static UtilityHelper getInstance() {
        return ourInstance;
    }

    private UtilityHelper() {

    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }



    public String getRandomJunk(){
        Random r = new Random();
        String alphabet = "1234567890qwertyuiopasdfghjklzxcvbnm";
        String junk = "";
        for (int i = 0; i < 5; i++) {
            junk = junk + alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return junk;
    }


    String sha256;

    public String getDeviceKey(Context context){
        String packageName = context.getApplicationContext().getPackageName();
        String version = "";
        StringBuilder deviceKey = new StringBuilder();
        try{

            version = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            //deviceKey.append(packageName);
            //deviceKey.append(version);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
            deviceKey.append(format.format(new Date()));

        }catch (PackageManager.NameNotFoundException ex){
            ex.printStackTrace();
        }
        return deviceKey.toString().replace(".","");
    }

    public boolean sessionInvalid(String err){
        if(err.equals("E006")){
            return true;
        }else{
            return false;
        }
    }


    public String getTodaysDate() {
        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return(String.valueOf(todaysDate));

    }

    public String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return(String.valueOf(currentTime));

    }



    /**
     * File manipulation
     *
     * */

    public boolean prepareDirectory(String tempDir)
    {
        try
        {
            if (makedirs(tempDir))
            {
                return true;
            } else {
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean makedirs(String dir)
    {
        File tempdir = new File(dir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory())
        {
            File[] files = tempdir.listFiles();
            for (File file : files)
            {
                if (!file.delete())
                {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }

    public String getStringFromInputStream(InputStream stream)
            throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer)))
            writer.write(buffer, 0, n);
        return writer.toString();
    }

    /**
     * Convert any written amount to currency format
     *
     * */

    public static TextWatcher amountWatcher(final EditText editText, final String metric, final int parseTo){
        return new TextWatcher() {
            String current = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString();

                    if (count != 0) {
                        String substr = cleanString.substring(cleanString.length() - 2);

                        if (substr.contains(".") || substr.contains(",")) {
                            cleanString += "0";
                        }
                    }

                    cleanString = cleanString.replaceAll("[,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    DecimalFormat df = new DecimalFormat(metric);
                    String formatted = df.format((parsed / parseTo));

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());
                    editText.addTextChangedListener(this);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        };
    }

    /**
     * This is used for how many decimal places transaction may take up
     *
     * */

    public String generateDecimalMetric(int decimal){
        StringBuilder s = new StringBuilder();
        for(int i = 1; i <= decimal; i++){
            s.append("0");
        }
        return s.toString();
    }

    public String getSimpleDate(Date currentDate){

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return df.format(currentDate);
    }

    public String geSimpleTime(Date currentDate){
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        return df.format(currentDate);

    }

    public String formatCurrency(Double amount){
        DecimalFormat formatter = new DecimalFormat("#,###,###.00");
        return formatter.format(amount);
    }

    /**
     * Connection Reachability
     *
     * */
    public boolean isNetworkConnected(Context context) {

        //TODO:- Add check what type of connectivity 3G or Wifi Connectivity
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Check if device is rooted
     *
     * */


    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    //
    public String formatByDecimalPlaces(double amount, int decimal){

        long integral = (long)amount;
        BigDecimal unscaled = new BigDecimal(integral);
        BigDecimal scaled = unscaled.scaleByPowerOfTen(-decimal);
        return String.valueOf(scaled);
    }



    public String getAppVersion(Context context){
        try{
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        }catch (Exception ex){
            ex.printStackTrace();
            return "Unversion";
        }
    }


    /**
     * Old methods returning boolean value
     *
     * */

    public boolean[] getOperationsAllowed(Long intPermission){

        //TODO:- Convert intPermission to binary
        //TODO:- Check edsels permission

        String str = Long.toBinaryString(intPermission);

//        try {
//            str = String.format("%032d", Long.parseLong(str));
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }

//        Log.d("_operation", str);

        StringBuilder permissionBinary = new StringBuilder();
        permissionBinary = permissionBinary.append(str).reverse();

        str = String.format("%-32s",permissionBinary.toString()).replace(' ','0');

//        Log.d("_operation", str.toString());

        boolean[] permissionResult = new boolean[str.length()];
        int i = 0;
        for(char ch: str.toString().toCharArray()){

            if(ch == '1'){
                permissionResult[i] = true;
            }else{
                permissionResult[i] = false;
            }

            i++;
        }

        return permissionResult;
    }

    public ArrayList<String> getPermissionArray(Long permission, int property, boolean isAdmin){

        boolean isAmountAsk = false;
        String str = Long.toBinaryString(permission);

        StringBuilder permissionBinary = new StringBuilder();
        permissionBinary = permissionBinary.append(str).reverse();

        str = String.format("%-32s",permissionBinary.toString()).replace(' ','0');

        ArrayList<String> captions = new ArrayList<>();
        ArrayList<String> commands = new ArrayList<>();
        ArrayList<String> imgids = new ArrayList<>();


        if(property == 1){
            //icon
            return imgids;
        }

        if(property == 2){
            //caption
            return captions;
        }

        if(property == 3){
            //command
            return commands;
        }

        return null;
    }


    public String formatTimeByTerminal(String str, int timeOrDate){
        StringBuilder strTime = new StringBuilder(str);

       if(strTime.toString() != "" || strTime.toString() != null) {
           if (timeOrDate == 1) {

               strTime.insert(2, "/");
               strTime.insert(5, "/");

           }

           if(timeOrDate == 0 && strTime.length() > 0){
               strTime.insert(2, ":");
               strTime.insert(5, ":");
           }

           return strTime.toString();

       }

       return strTime.toString();

    }

    public String padNum(int number){
        return String.format("%4s", number);
    }




    static class CurrencyUtils {

        public static SortedMap<Currency, Locale> currencyLocaleMap;

        static {
            currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
                public int compare(Currency c1, Currency c2) {
                    return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
                }
            });
            for (Locale locale : Locale.getAvailableLocales()) {
                try {
                    Currency currency = Currency.getInstance(locale);
                    currencyLocaleMap.put(currency, locale);
                } catch (Exception e) {
                }
            }
        }


        public static String getCurrencySymbol(String currencyCode) {
            Currency currency = Currency.getInstance(currencyCode);
            System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
            return currency.getSymbol(currencyLocaleMap.get(currency));
        }

    }




}
