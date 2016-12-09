package bsu.fpmi.pharmacy.pharmacy_mobile.serialize;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;

/**
 * Created by annashunko
 */

public class MedicineSerializer extends BaseSerializer<Medicine> {
    @Override
    public Medicine deserializeModel(String serializedModel) {
        return gson.fromJson(serializedModel, Medicine.class);
    }
}
