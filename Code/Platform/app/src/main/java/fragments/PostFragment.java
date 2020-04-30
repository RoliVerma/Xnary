package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devhack.platform.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    TextView textView;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        //textView=view.findViewById(R.id.post_text);
        return view;
    }
}
