package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    private SwipeRefreshLayout mSwipeLayout;
    private RelativeLayout emptyView;

    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_medicine);
        listView.setDividerHeight(0);
        emptyView = (RelativeLayout) findViewById(R.id.empty_layout);
        emptyView.setVisibility(View.GONE);

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
                        if (response != null)
                            medicineList = response.body();
                        setAdapter();
                        mSwipeLayout.setRefreshing(false);
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


        setListOnClickListeners();
    }

    public void setAdapter() {
        adapter = new MedicineAdapter(this, medicineList, user);
        if (medicineList.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    private void setListOnClickListeners() {
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
        if (user != null)
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    new AlertDialog.Builder(MedicineActivity.this)
                            .setTitle(R.string.deleting_medicine)
                            .setMessage(R.string.confirm_delete_medicine)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final MedicineService medicineService = PharmacyRESTService.medicineService();
                                    medicineService.medicineDelete(medicineList.get(i).idMedicine).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            if (response.body() != null)
                                            if (response.body().equals(200)) {
                                                medicineList.remove(i);
                                                setAdapter();
                                                Toast.makeText(getApplicationContext(), R.string.med_deleted, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                    return  true;
                }
            });
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
