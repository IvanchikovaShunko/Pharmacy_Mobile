package bsu.fpmi.pharmacy.pharmacy_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.entity.Medicine;



public class InstructionsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;

    List<Medicine> medicines;

    public InstructionsAdapter(Context context, List<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return medicines.size();
    }

    @Override
    public Object getItem(int i) {
        return medicines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View cView = view;
        if (cView == null) {
            cView = lInflater.inflate(R.layout.item_instruction, viewGroup, false);
        }
        Medicine medicine = (Medicine) getItem(i);

        ((TextView) cView.findViewById(R.id.medicine_name)).setText(medicine.getName());
        ((TextView) cView.findViewById(R.id.medicine_state)).setText(medicine.getState() + ", " + medicine.getWeight() + " г");

        return cView;
    }
}
