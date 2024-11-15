package student.inti.a202sgi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameEditText, ageEditText, genderEditText, descriptionEditText;
    private Button saveChangesButton;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        loadUserProfile();

        saveChangesButton.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "id = ?", new String[]{"1"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            nameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ageEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("age")));
            genderEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            descriptionEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            cursor.close();
        }
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("gender", gender);
        values.put("description", description);

        long result = db.update("users", values, "id = ?", new String[]{"1"});

        if (result != -1) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}





