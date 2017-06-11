package io.branio.paybills.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.branio.paybills.model.Bill;

/**
 * Created by anjose on 6/10/17.
 */

public class BillsParser {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static ArrayList<Bill> getBills(JSONObject response) {
        ArrayList<Bill> bills = new ArrayList<>();
        try {
            JSONArray data = response.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject dataJSON = data.getJSONObject(i);
                JSONObject billJSON = dataJSON.getJSONObject("attributes");
                Bill bill = new Bill();
                bill.setName(billJSON.getString("name"));
                bill.setMonth(billJSON.getInt("month"));
                bill.setValue(billJSON.getDouble("cost"));
                bill.setCompany(billJSON.getString("company"));
                String dateString = billJSON.getString("limit-date");
                bill.setType(billJSON.getInt("type"));
                bill.setPaid(billJSON.getInt("status") == 1);
                try {
                    bill.setDueDate(sdf.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bills.add(bill);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bills;
    }

}
