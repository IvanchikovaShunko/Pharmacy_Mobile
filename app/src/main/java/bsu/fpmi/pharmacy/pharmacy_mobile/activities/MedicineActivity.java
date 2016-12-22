package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.MedicineAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.MedicineService;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.MedicineSerializer;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private MedicineAdapter adapter;
    private ProgressDialog progressDialog;

    List<Medicine> medicineList = new ArrayList<>();

    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_medicine);
        listView.setDividerHeight(0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");

        MedicineService medicineService = PharmacyRESTService.medicineService();
        progressDialog.show();
        medicineService.medicineList().enqueue(new Callback<List<Medicine>>() {
            @Override
            public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                medicineList = response.body();
                setAdapter();
                progressDialog.hide();
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

    public void setAdapter() {
        adapter = new MedicineAdapter(this, medicineList, user);
        listView.setAdapter(adapter);
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_medicine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null && user.role.equalsIgnoreCase("admin")) {
            MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.main_menu_admin, menu);
            return true;
        } else {
            super.onCreateOptionsMenu(menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_medicine) {
            Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("USER", new UserSerializer().serializeModel(user));
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
