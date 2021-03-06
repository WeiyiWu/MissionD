package com.example.missiond;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ViewfinderView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.fragment.app.DialogFragment;

import java.util.List;

/**
 * Displays driver's information
 * if rider confirms drivers, the go to the next activity, else find another driver
 * @author
 *  Weiyi Wu
 * @version
 *  Mar.12 2020
 */
public class RiderConfirmDriverDialog extends DialogFragment {
    private Button email,call,cancel,confirm;
    private RiderConfirmDriverListener listener;
    private String rider_name;
    private String phone;
    private String emailAddr;

    public interface RiderConfirmDriverListener{
        void onConfirmClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.rider_confirm_driver_dialog,container,false);

        /*
        set driver's TextView: rating, plate num, name
        get driver's button: phone, email
         */
        final DataBaseHelper DB = DataBaseHelper.getInstance();
        rider_name = getArguments().getString(rider_name);
        DB.GetUserOrders(rider_name, new Consumer<List<Order>>() {
            @Override
            public void accept(List<Order> orders) {
                Order active_order;
                for (int i=0; i < orders.size();i++){
                    if (orders.get(i).orderStatus == 2);
                    active_order = orders.get(i);
                    String driver_name = active_order.getDriver();
                    DB.getDriver(driver_name, new Consumer<Driver>() {
                        @Override
                        public void accept(Driver driver) {
                            onLoaded(v,driver);
                        }
                    });
                    break;
                }

            }
        });
        call = v.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone == null){
                    Toast.makeText(getActivity(),"phone==null",Toast.LENGTH_LONG).show();
                    return;
                }
                String s = "tel:" + phone;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(s));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    startActivity(callIntent);
                }
            }
        });


        email = v.findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailAddr == null){
                    Toast.makeText(getActivity(),"email==null",Toast.LENGTH_LONG).show();
                    return;
                }
                /**
                 * https://stackoverflow.com/questions/28588255/no-application-can-perform-this-action-when-send-email
                 * @author
                 * yubaraj poudel
                 */
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddr});
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        cancel = v.findViewById(R.id.cancel_river_confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        confirm = v.findViewById(R.id.confirm_river_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmClick();
                getDialog().dismiss();
            }
        });

        return v;
    }

    public void onLoaded(View v ,Driver driver){

        final TextView rating = v.findViewById(R.id.driver_rating);

        float driver_rating = driver.getRating();
        rating.setText(String.valueOf(driver_rating));
        phone = driver.getPhoneNumber();
        emailAddr = driver.getEmailAddress();

        String driver_name = driver.getUserName();

        TextView name = v.findViewById(R.id.driver_name);
        name.setText(driver_name);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RiderConfirmDriverListener){
            listener = (RiderConfirmDriverListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement RiderConfirmDriverListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
