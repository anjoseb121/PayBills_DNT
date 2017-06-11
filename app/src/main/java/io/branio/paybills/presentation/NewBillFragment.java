package io.branio.paybills.presentation;


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

    EditText editValue, editCompany, editName;
    DatePicker dateMonth, dateDue;
    Spinner spinnerType;
    Switch swPaid;
    Button buttonSave;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private AsyncHttpClient client;

    public NewBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new AsyncHttpClient();
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

        // assign events
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBill();
            }
        });
        return root;
    }

    private void saveBill() {
        //Calendar newDate = Calendar.getInstance();
        int type = spinnerType.getSelectedItemPosition();
        Double value = Double.parseDouble(editValue.getText().toString());
        int month = dateMonth.getMonth();
        // due date
        //newDate.set(dateDue.getYear(), dateDue.getMonth(), dateDue.getDayOfMonth());
        Date due = new Date(dateDue.getYear(), dateDue.getMonth(), dateDue.getDayOfMonth());
        String company = editCompany.getText().toString();
        String name = editName.getText().toString();
        boolean paid = swPaid.isChecked();
        Bill bill = new Bill(name, month, company, value, type, due, paid);

        saveBillToService(bill);
    }

    public void saveBillToService(Bill b) {
        // params to request
        RequestParams params = new RequestParams();
        String url = BASE_URL;
        params.add("name", b.getName());
        params.add("month", String.valueOf(b.getMonth()));
        params.add("cost", String.valueOf(b.getValue()));
        params.add("type", String.valueOf(b.getType()));
        params.add("company", b.getCompany());
        params.add("status", String.valueOf(b.isPaid() ? 1 : 0));
        params.add("limit-date", String.valueOf(b.getDueDate()));

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
                Log.e("AntonioBar", "response ->"+errorResponse.toString());
                Toast.makeText(getActivity(), "Error "+statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
