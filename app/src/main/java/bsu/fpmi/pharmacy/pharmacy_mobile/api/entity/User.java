package bsu.fpmi.pharmacy.pharmacy_mobile.api.entity;

import java.util.List;

/**
 * Created by annashunko
 */

public class User {
    public int id;
    public String password;
    public String username;
    public String role;
    public UserDetail userDetail;
    private List<Subscription> subscriptions;
}
