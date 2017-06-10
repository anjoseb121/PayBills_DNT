package io.branio.paybills;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewBillFragment extends Fragment {

    EditText editValue, editCompany;
    DatePicker dateMonth, dateDue;
    Spinner spinnerType;
    Switch swPaid;
    Button buttonSave;

    public NewBillFragment() {
        // Required empty public constructor
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
        String type = spinnerType.getSelectedItem().toString();
        Double value = Double.parseDouble(editValue.getText().toString());
        String month = "Month "+dateMonth.getMonth();
        String due = "Month: "+dateDue.getMonth()+" Day: "+dateDue.getDayOfMonth()+" Year: "+dateDue.getYear();
        String company = editCompany.getText().toString();
        boolean paid = swPaid.isChecked();
        Bill bill = new Bill(month, company, value, type, due, paid);

        BillsClient billClient = new BillsClient();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                Toast.makeText(getActivity(), "Bill saved", Toast.LENGTH_SHORT).show();
            }
        };
        billClient.saveBill(bill, callback);
    }

}
