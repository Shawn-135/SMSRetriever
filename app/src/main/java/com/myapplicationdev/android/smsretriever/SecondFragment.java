package com.myapplicationdev.android.smsretriever;

import android.content.ContentResolver;
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

public class SecondFragment extends Fragment {

    TextView tvFrag2, tvSMS;
    EditText etSMS;
    Button btnRetrieveSMS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        btnRetrieveSMS = view.findViewById(R.id.btnRetrieveSMS);
        tvSMS = view.findViewById(R.id.tvSMS);
        etSMS = view.findViewById(R.id.etSMS);
        tvFrag2 = view.findViewById(R.id.tvFrag2);

        btnRetrieveSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etSMS.getText().toString().trim().length() != 0){
                    tvSMS.setText("");
                    String text = etSMS.getText().toString();
                    String [] textArr = text.split("\\s");

                    for (int i = 0; i < textArr.length; i++){
                        Uri uri = Uri.parse("content://sms");
                        String[] reqCols = new String[]{"date", "address", "body", "type"};

                        ContentResolver cr = getActivity().getContentResolver();

                        String filter ="body LIKE ?";
                        String[] filterArgs = {"%" + textArr[i] + "%"};

                        Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                        String smsBody = "";
                        if (cursor.moveToFirst()) {
                            do {
                                long dateInMillis = cursor.getLong(0);
                                String date = (String) DateFormat.format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                                String address = cursor.getString(1);
                                String body = cursor.getString(2);
                                String type = cursor.getString(3);
                                if (type.equalsIgnoreCase("1")) {
                                    type = "Inbox:";
                                } else {
                                    type = "Sent:";
                                }
                                smsBody += type + " " + address + "\n at " + date
                                        + "\n\"" + body + "\"\n\n";
                            } while (cursor.moveToNext());
                        }
                        tvSMS.append(smsBody);
                    }//end of for loop

                }//end of if
                else{
                    Toast.makeText(getActivity(), "Please enter a word", Toast.LENGTH_LONG).show();
                }//end of text field validation

            }
        });

        return view;
    }
}