package student.inti.a202sgi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private LinearLayout jobContainer;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        jobContainer = view.findViewById(R.id.jobContainer);
        dbRef = FirebaseDatabase.getInstance().getReference("jobs");

        loadSampleJobs(); // Load sample job listings

        return view;
    }

    private void loadSampleJobs() {
        // Sample data
        String[] jobTitles = {"Software Engineer", "Project Manager", "UI/UX Designer"};
        String[] companies = {"TechCorp", "Innovatech", "DesignPlus"};
        String[] locations = {"New York, NY", "San Francisco, CA", "Austin, TX"};

        for (int i = 0; i < jobTitles.length; i++) {
            TextView jobTextView = new TextView(getContext());
            jobTextView.setText(String.format("Title: %s\nCompany: %s\nLocation: %s\n",
                    jobTitles[i], companies[i], locations[i]));
            jobTextView.setPadding(0, 16, 0, 16);

            jobContainer.addView(jobTextView);
        }
    }
}


