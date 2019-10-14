package edu.bluejack19_1.BloodFOR.Model;

public class User {

    public String firstName, lastName, email, password, gender;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastName, String email, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
    private void writeNewUser(String userId, String name, String email) {
//        User user = new User(name, email);

//        donorda.child("users").child(userId).setValue(user);
    }
}
