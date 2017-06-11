package io.branio.paybills.presentation;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import io.branio.paybills.R;
import io.branio.paybills.model.Bill;

import static io.branio.paybills.provider.BillsClient.BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewBillFragment extends Fragment {

    Calendar calendar;
    EditText editValue, editCompany, editName;
    TextView dateMonth, dateDue;
    Spinner spinnerType;
    Switch swPaid;
    Button buttonSave;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private AsyncHttpClient client;
    Date monthDate, dueDate;

    public NewBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new AsyncHttpClient();
        calendar = Calendar.getInstance();
        monthDate = calendar.getTime();
        dueDate = calendar.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_bill, container, false);
        editValue = root.findViewById(R.id.edit_value);
        editCompany = root.findViewById(R.id.edit_company);
        dateMonth = root.findViewById(R.id.date_month);
        dateDue = root.findViewById(R.id.date_due);
        spinnerType = root.findViewById(R.id.spin_type);
        swPaid = root.findViewById(R.id.switch1);
        buttonSave = root.findViewById(R.id.button_save);
        editName = root.findViewById(R.id.edit_name);
        dateMonth.setText(String.valueOf(monthDate));
        dateDue.setText(String.valueOf(dueDate));

        // assign events
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBill();
            }
        });
        dateMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendatepicker();
            }
        });
        dateDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendatepickerDue();
            }
        });
        return root;
    }

    private void saveBill() {
        //Calendar newDate = Calendar.getInstance();
        int type = spinnerType.getSelectedItemPosition();
        Double value = Double.parseDouble(editValue.getText().toString());
        String company = editCompany.getText().toString();
        String name = editName.getText().toString();
        boolean paid = swPaid.isChecked();
        Bill bill = new Bill(name, monthDate.getMonth(), company, value, type, dueDate, paid);

        saveBillToService(bill);
    }

    public void saveBillToService(Bill b) {
        // params to request
        RequestParams params = new RequestParams();
        String url = BASE_URL;
        params.add("name", b.getName());
        params.add("month", String.valueOf(b.getMonth()));
        params.add("cost", String.valueOf(b.getValue()));
        params.add("type", String.valueOf(b.getType()+1));
        params.add("company", b.getCompany());
        params.add("status", String.valueOf(b.isPaid() ? 1 : 0));
        params.add("limitDate", sdf.format(dueDate));
        Log.v("AntonioBar", "Due date"+sdf.format(dueDate));
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("AntonioBar", "response->"+response.toString());
                Toast.makeText(getActivity(), "Bill saved", Toast.LENGTH_SHORT).show();
                editName.setText("");
                editCompany.setText("");
                editValue.setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //Log.e("AntonioBar", "response ->"+errorResponse.toString());
                //Toast.makeText(getActivity(), "Error "+statusCode, Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    public void opendatepicker(){
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // Toast.makeText(getActivity(),""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth,Toast.LENGTH_SHORT).show();
                monthDate = new Date(year-1900, monthOfYear, dayOfMonth);
                dateMonth.setText(String.valueOf(monthDate));
            }
        };

        DatePickerDialog d = new DatePickerDialog(getActivity(),
                mDateSetListener, monthDate.getYear(), monthDate.getMonth(), monthDate.getDay());
        d.show();
    }

    public void opendatepickerDue(){
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // Toast.makeText(getActivity(),""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth,Toast.LENGTH_SHORT).show();
                dueDate = new Date(year-1900, monthOfYear, dayOfMonth);
                dateDue.setText(String.valueOf(dueDate));

            }
        };

        DatePickerDialog d = new DatePickerDialog(getActivity(),
                mDateSetListener, dueDate.getYear(), dueDate.getMonth(), dueDate.getDay());
        d.show();
    }



}
