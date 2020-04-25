package djpiper28.mtgnewsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ErrorFragment extends Fragment {

    private String errorMessage;

    public ErrorFragment(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ErrorFragment newInstance(String errorMessage) {
        ErrorFragment fragment = new ErrorFragment(errorMessage);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        TextView error = (TextView) view.findViewById(R.id.errorMessage);
        error.setText(this.errorMessage);
        return view;
    }
}
