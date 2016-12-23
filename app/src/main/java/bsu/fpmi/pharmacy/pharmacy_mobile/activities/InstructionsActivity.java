package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.InstructionsAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.MedicineAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.MedicineService;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.MedicineSerializer;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructionsActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private InstructionsAdapter adapter;
    private ProgressDialog progressDialog;

    List<Medicine> medicineList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void initActivityGUI() {;
        listView = (ListView) findViewById(R.id.list_view_instructions);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.loading));

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MedicineService medicineService = PharmacyRESTService.medicineService();
                medicineService.medicineList().enqueue(new Callback<List<Medicine>>() {
                    @Override
                    public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                        mSwipeLayout.setRefreshing(false);
                        medicineList = response.body();
                        setAdapter();
                    }

                    @Override
                    public void onFailure(Call<List<Medicine>> call, Throwable t) {
                        mSwipeLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        MedicineService medicineService = PharmacyRESTService.medicineService();
        progressDialog.show();
        medicineService.medicineList().enqueue(new Callback<List<Medicine>>() {
            @Override
            public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                progressDialog.hide();
                medicineList = response.body();
                setAdapter();
            }

            @Override
            public void onFailure(Call<List<Medicine>> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), MedicineInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("USER", new UserSerializer().serializeModel(user));
                intent.putExtra("MEDICINE", new MedicineSerializer().serializeModel(medicineList.get(i)));
                startActivity(intent);
            }
        });

    }

    private void setAdapter() {
        adapter = new InstructionsAdapter(this, medicineList);
        listView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_instructions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
