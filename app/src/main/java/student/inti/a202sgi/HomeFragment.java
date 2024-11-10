package student.inti.a202sgi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<Job> jobList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy job data for Malaysia-based jobs
        jobList = new ArrayList<>();
        jobList.add(new Job("Software Engineer", "TechCorp", "Kuala Lumpur, Malaysia"));
        jobList.add(new Job("Project Manager", "Innovatech", "Penang, Malaysia"));
        jobList.add(new Job("UI/UX Designer", "DesignHub", "Johor Bahru, Malaysia"));
        jobList.add(new Job("Marketing Specialist", "BizSolutions", "Shah Alam, Malaysia"));
        jobList.add(new Job("Data Scientist", "DataPro", "Cyberjaya, Malaysia"));

        jobAdapter = new JobAdapter(jobList);
        recyclerView.setAdapter(jobAdapter);

        return rootView;
    }
}


