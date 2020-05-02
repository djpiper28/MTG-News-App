package djpiper28.mtgnewsapp.newscontainers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import djpiper28.mtgnewsapp.LoadingScreen;
import djpiper28.mtgnewsapp.R;
import djpiper28.mtgnewsapp.RecycleViewerAdapterSets;
import forohfor.scryfall.api.Set;

import static djpiper28.mtgnewsapp.LoadingScreen.onRefresh;

public class SetPreviewContainer extends Fragment {

    private List<Set> sets;
    private static final int max_sets = 5;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_container, container, false);

        List unreleasedSets = new LinkedList<>();
        List releasedSets = new LinkedList<>();

        Date now = Calendar.getInstance().getTime();
        Date twoWeeksAgo = Calendar.getInstance().getTime();
        twoWeeksAgo.setTime(twoWeeksAgo.getTime() - 14l * 24l * 36000l);

        int newSetCount = 0;

        for (forohfor.scryfall.api.Set set : sets) {
            if (set.getParentSetCode() == null || set.getParentSetCode() == "") {
                if (set.getReleasedAt().after(now)) {
                    unreleasedSets.add(set);
                } else if (set.getReleasedAt().after(twoWeeksAgo) && newSetCount < 5) {
                    releasedSets.add(set);
                    newSetCount++;
                } else if (newSetCount < max_sets) {
                    releasedSets.add(set);
                    newSetCount++;
                } else {
                    break;
                }
            }
        }

        // Inflate the recycle views

        // UnreleasedSets
        RecyclerView recyclerViewUnreleasedSets = view.findViewById(R.id.UnreleasedSets);
        recyclerViewUnreleasedSets.setLayoutManager(new LinearLayoutManager(getContext()));
        RecycleViewerAdapterSets adapterUnreleasedSets = new RecycleViewerAdapterSets(getContext(), unreleasedSets);
        recyclerViewUnreleasedSets.setAdapter(adapterUnreleasedSets);

        // ReleasedSets
        RecyclerView recyclerViewReleasedSets = view.findViewById(R.id.NewSets);
        recyclerViewReleasedSets.setLayoutManager(new LinearLayoutManager(getContext()));
        RecycleViewerAdapterSets adapterReleasedSets = new RecycleViewerAdapterSets(getContext(), releasedSets);
        recyclerViewReleasedSets.setAdapter(adapterReleasedSets);


        // add on refresh
        onRefresh.add(() -> {
            int i = 0;

            while(!unreleasedSets.isEmpty()){
                unreleasedSets.remove(0);
            }

            while(!releasedSets.isEmpty()){
                releasedSets.remove(0);
            }

            for (forohfor.scryfall.api.Set set : sets) {
                if (set.getParentSetCode() == null || set.getParentSetCode().equals("")) {
                    if (set.getReleasedAt().after(now)) {
                        unreleasedSets.add(set);
                    } else if (set.getReleasedAt().after(twoWeeksAgo) && i < 5) {
                        releasedSets.add(set);
                        i++;
                    } else if (i < max_sets) {
                        releasedSets.add(set);
                        i++;
                    } else {
                        break;
                    }
                }
            }

            adapterReleasedSets.notifyDataSetChanged();
            adapterUnreleasedSets.notifyDataSetChanged();
        });

        return view;
    }
}
