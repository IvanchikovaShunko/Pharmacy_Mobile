package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.MedicineAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.entity.Medicine;

public class MedicineActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private MedicineAdapter adapter;

    List<Medicine> medicineList = new ArrayList<>();

    @Override
    protected void initActivityGUI() {
        fillList();
        listView = (ListView) findViewById(R.id.list_view_medicine);
        adapter = new MedicineAdapter(this, medicineList);
        listView.setAdapter(adapter);
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
        medicineList.add(new Medicine(1, "Но-шпа", "таблетки", null, 200, 12, null));
        medicineList.add(new Medicine(1, "Терафлю", "порошок", null, 320, 12, null));
        medicineList.add(new Medicine(1, "Валидол", "таблетки", null, 150, 12, null));
    }
}
