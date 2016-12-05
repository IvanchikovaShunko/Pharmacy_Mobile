package bsu.fpmi.pharmacy.pharmacy_mobile.serialize;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;

/**
 * Created by annashunko
 */

public class UserSerializer extends BaseSerializer<User> {
    @Override
    public User deserializeModel(String serializedModel) {
        return gson.fromJson(serializedModel, User.class);
    }
}
