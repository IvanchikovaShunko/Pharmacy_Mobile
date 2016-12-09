package bsu.fpmi.pharmacy.pharmacy_mobile.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;

public class MedicineAdapter extends BaseAdapter{
    Context context;
    LayoutInflater lInflater;

    List<Medicine> medicines;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
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
            cView = lInflater.inflate(R.layout.item_medicine, viewGroup, false);
        }
        Medicine medicine = (Medicine) getItem(i);

        ((TextView) cView.findViewById(R.id.medicine_name)).setText(medicine.nameMedicine);
        ((TextView) cView.findViewById(R.id.medicine_state)).setText(medicine.state + ", " + medicine.cost + " $");

        ImageView addToCartImageView = (ImageView) cView.findViewById(R.id.add_to_cart_imageView);
        addToCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Medicine added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        return cView;
    }
}
