package com.example.missiond;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Ask driver to confirm the money that will be paid on that trip
 * @author
 *  Weiting Chi
 * @version
 *  Mar.12 2020
 */

public class IsMoneyOKFragment extends DialogFragment {

    private Button close_button;
    private Button confirm_button;
    public String Location;
    public String Destination;
    private String Rider;
    private TextView Money;

    private float startLat,startLng,endLat,endLng,Cost;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.is_money_ok_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle!=null){
            Location = bundle.getString("trip_location");
            Destination = bundle.getString("trip_destination");
            startLat = bundle.getFloat("startLocationLat");
            startLng = bundle.getFloat("startLocationLng");
            endLat =bundle.getFloat("endLocationLat");
            endLng = bundle.getFloat("endLocationLng");
            Rider = bundle.getString("rider");
            Cost = bundle.getFloat("cost");

        }

        close_button = v.findViewById(R.id.close_button);
        confirm_button = v.findViewById(R.id.ConfirmMoney_button);
        Money = v.findViewById(R.id.money);
        Money.setText(String.valueOf((int)Cost));


        /**
         * click the close button
         * the dialog will dismiss
         */
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG,"onClick:closing dialog");
                getDialog().dismiss();
            }
        });

        /**
         * press the confirm button
         * the dialog will dismiss first
         * then, will show the driver wait rider confirm Fragment
         */
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("trip_location",Location);
                bundle.putString("trip_destination",Destination);
                bundle.putFloat("startLocationLat",startLat);
                bundle.putFloat("startLocationLng",startLng);
                bundle.putFloat("endLocationLat",endLat);
                bundle.putFloat("endLocationLng",endLng);
                bundle.putString("rider",Rider);
                DriverWaitRiderConfrimFragment fragment = new DriverWaitRiderConfrimFragment();
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(),"Waiting");
            }
        });

        return v;

        }
}
