package djpiper28.mtgnewsapp.setpreview;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import djpiper28.mtgnewsapp.LoadingScreen;
import djpiper28.mtgnewsapp.R;
import forohfor.scryfall.api.Set;

import static djpiper28.mtgnewsapp.LoadingScreen.onRefresh;

public class SetPreviewContainer extends Fragment {

    private static final int max_sets = 5;
    private final List<Set> sets;

    public SetPreviewContainer() {
        this.sets = LoadingScreen.sets;
        if (sets == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_container, container, false);

        Date now = Calendar.getInstance().getTime();

        int unreleasedPointer = 0;
        for (Set set : sets) {
            if (set.getParentSetCode() == null || set.getParentSetCode().equals("") || set.getSetType().equalsIgnoreCase("starter")) {
                if (set.getReleasedAt().after(now)) {
                    unreleasedPointer++;
                } else {
                    break;
                }
            }
        }

        // Inflate the recycle views

        // UnreleasedSets
        RecyclerView recyclerViewUnreleasedSets = view.findViewById(R.id.sets_container);
        recyclerViewUnreleasedSets.setLayoutManager(new LinearLayoutManager(getContext()));
        RecycleViewerAdapterSets setsRecyclerViewerAdapter = new RecycleViewerAdapterSets(getContext(), sets, unreleasedPointer);
        recyclerViewUnreleasedSets.setAdapter(setsRecyclerViewerAdapter);

        // add on refresh
        onRefresh.add(() -> {
            while (!sets.isEmpty())
                sets.remove(0);

            int unreleasedPointer2 = 0;
            for (Set set : sets) {
                if (set.getParentSetCode() == null || set.getParentSetCode().equals("") || set.getSetType().equalsIgnoreCase("starter")) {
                    if (set.getReleasedAt().after(now)) {
                        unreleasedPointer2++;
                    } else {
                        break;
                    }
                }
            }

            setsRecyclerViewerAdapter.notifyDataSetChanged();
            setsRecyclerViewerAdapter.setPointer(unreleasedPointer2);
        });

        return view;
    }
}
