package com.myapplicationdev.android.smsretriever;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FirstFragment extends Fragment {


    EditText etFrag1;
    Button btnF1RetrieveSMS;
    Button btnEmailSMS;
    TextView tvFrag1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =  inflater.inflate(R.layout.fragment_first, container, false);

        tvFrag1 = view.findViewById(R.id.tvFrag1);
        etFrag1 = view.findViewById(R.id.etFrag1);
        btnF1RetrieveSMS = view.findViewById(R.id.btnF1RetrieveSMS);
        btnEmailSMS = view.findViewById(R.id.btnEmailSMS);


        btnF1RetrieveSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etFrag1.getText().toString().trim().length() != 0){

                    String number = etFrag1.getText().toString();
                    Uri uri = Uri.parse("content://sms");
                    String[] reqCols = new String[]{"date", "address", "body", "type"};

                    ContentResolver cr = getActivity().getContentResolver();

                    String filter="address LIKE ? AND type = ?";

                    String[] filterArgs = {number, "1"};

                    Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                    String smsBody = "";
                    if (cursor.moveToFirst()) {
                        do {
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat
                                    .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(  1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            if (type.equalsIgnoreCase("1")) {
                                type = "Inbox:";
                            }
                            smsBody += type + " " + address + "\n at " + date
                                    + "\n\"" + body + "\"\n\n";
                        } while (cursor.moveToNext());
                    }

                    tvFrag1.setText(smsBody);
                }//end of if

                else{
                    Toast.makeText(getActivity(), "Please enter a number", Toast.LENGTH_LONG).show();
                }//end of text field validation

            }
        });//end of btnRetrieveSMS


        btnEmailSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);

                email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"myemail@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT,
                        "SMS Content");
                email.putExtra(Intent.EXTRA_TEXT,
                        tvFrag1.getText());

                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email,
                        "Choose an Email client :"));
            }
        });//end of emailSMS


       return view;
    }//end of onCreateView
}//end of class