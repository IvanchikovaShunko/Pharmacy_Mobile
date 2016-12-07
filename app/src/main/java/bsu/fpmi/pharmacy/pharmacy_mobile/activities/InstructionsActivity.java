package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.InstructionsAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.MedicineAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;

public class InstructionsActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private InstructionsAdapter adapter;

    List<Medicine> medicineList = new ArrayList<>();

    @Override
    protected void initActivityGUI() {
        fillList();
        listView = (ListView) findViewById(R.id.list_view_instructions);
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

    private void fillList() {
//        medicineList.add(new Medicine(1, "Но-шпа", "таблетки", null, 200, 12, null));
//        medicineList.add(new Medicine(1, "Терафлю", "порошок", null, 320, 12, null));
//        medicineList.add(new Medicine(1, "Валидол", "таблетки", null, 150, 12, null));
//        medicineList.add(new Medicine(1, "Антифлу", "капсулы", null, 150, 12, null));
//        medicineList.add(new Medicine(1, "Доритрицин", "таблетки", null, 150, 12, null));

    }
}
