package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.MedicineAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.MedicineResult;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.MedicineService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MedicineActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private MedicineAdapter adapter;
    private ProgressDialog progressDialog;

    List<Medicine> medicineList = new ArrayList<>();

    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_medicine);

        MedicineService medicineService = PharmacyRESTService.medicineService();
        medicineService.medicineList().enqueue(new Callback<List<Medicine>>() {
            @Override
            public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                medicineList = response.body();
                setAdapter();
            }

            @Override
            public void onFailure(Call<List<Medicine>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setAdapter() {
        adapter = new MedicineAdapter(this, medicineList);
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

    private void fillList() {
//        medicineList.add(new Medicine(1, "Но-шпа", "таблетки", null, 200, 12, null));
//        medicineList.add(new Medicine(1, "Терафлю", "порошок", null, 320, 12, null));
//        medicineList.add(new Medicine(1, "Валидол", "таблетки", null, 150, 12, null));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null && user.role.equalsIgnoreCase("admin")) {
            MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_medicine) {
            Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
