package io.branio.paybills.presentation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.branio.paybills.R;
import io.branio.paybills.adapter.BillsAdapter;
import io.branio.paybills.model.Bill;
import io.branio.paybills.parser.BillsParser;
import io.branio.paybills.provider.BillsClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListBIllsFragment extends Fragment {

    RecyclerView recyclerView;
    Spinner spinnerQuery;
    List<Bill> bills;
    AsyncHttpClient client;

    public ListBIllsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bills = new ArrayList<>();
         client = new AsyncHttpClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_bills, container, false);
        recyclerView = root.findViewById(R.id.recycler_bills);
        spinnerQuery = root.findViewById(R.id.spinner);

        // assign events
        spinnerQuery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                requestBillBy(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*BillsAdapter adapter = new BillsAdapter(bills);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        return root;
    }

    public void requestBillBy(int position) {
        String url = "";
        String filter = "";
        String value = "";
        if (position >= 0 && position <= 11) {
            filter = "month";
            value = String.valueOf(position);
        } else if (position >= 12 && position <= 15) {
            filter = "type";
            // substrates eleven to position to use types 1,2,3,4
            value = String.valueOf(position-11);
        } else if (position == 16) {
            filter = "paid";
        }
        switch (filter) {
            case "month":
                url = BillsClient.BASE_URL+"/month/"+value;
                break;
            case "type":
                url = BillsClient.BASE_URL+"/type/"+value;
                break;
            case "paid":
                url = BillsClient.BASE_URL+"/paid";
                break;
        }
        client.get(url,null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.v("AntonioBar", "response ->"+response.toString());
                bills = BillsParser.getBills(response);
                BillsAdapter adapter = new BillsAdapter(bills);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //Log.e("AntonioBar", "response ->"+errorResponse.toString());
            }
        });
    }

}
