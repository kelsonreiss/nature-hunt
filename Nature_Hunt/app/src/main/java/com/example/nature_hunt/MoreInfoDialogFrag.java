package com.example.nature_hunt;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MoreInfoDialogFrag extends DialogFragment {
    private Hunt hunt;


    static MoreInfoDialogFrag getInstance(Hunt hunt){
        MoreInfoDialogFrag frag = new MoreInfoDialogFrag();

        // Pass in hunt
        Bundle args = new Bundle();
        args.putSerializable("hunt", hunt);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            hunt = (Hunt) getArguments().getSerializable("hunt");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.more_info_frag, container, false);

        if (hunt != null){
                // Set title
                TextView title = (TextView) v.findViewById(R.id.hunt_title_more_info);
                title.setText(hunt.name());
                if (hunt.friendlyLocation() != null){
                    // Set location
                    TextView location = (TextView) v.findViewById(R.id.hunt_location_more_info_frag);
                    location.setText(hunt.friendlyLocation());
                }

                if (hunt.description() != null){
                    // Set description
                    TextView description = (TextView) v.findViewById(R.id.hunt_description_more_info_frag);
                    description.setText(hunt.description());
                } else{
                    // Hide Description title
                    TextView description_title = (TextView)v.findViewById(R.id.description_title_more_info_frag);
                    description_title.setVisibility(View.GONE);
                }
        }

        // Set dismiss button
        Button dismiss = (Button) v.findViewById(R.id.dismiss_more_info_frag);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }
}
