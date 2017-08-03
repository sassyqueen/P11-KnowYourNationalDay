
package demodialogs.android.myapplicationdev.com.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> al;
    ArrayAdapter aa;
    String items;
    String accessCode;
    EditText etPassphrase;
    LinearLayout passPhrase;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String key = prefs.getString("key", "");
        items = "";
        score = 0;

        if (key.equals("")){
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.passphrase, null);
            etPassphrase = (EditText) passPhrase
                    .findViewById(R.id.editTextPassPhrase);



            builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Please login")
                    .setView(passPhrase)
                    .setNegativeButton("No Access Code", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (etPassphrase.getText().toString().equals("738964")) {
                                {

                                    lv = (ListView) findViewById(R.id.listview);
                                    al = new ArrayList<>();
                                    aa = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, al);
                                    al.add("Singapore's National Day is on 9 Aug");
                                    al.add("Singapore is 52 years old");
                                    al.add("Theme is: #OneNationTogether");
                                    lv.setAdapter(aa);
                                    aa.notifyDataSetChanged();
                                    accessCode = etPassphrase.getText().toString();
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    final SharedPreferences.Editor prefEdit = prefs.edit();
                                    prefEdit.putString("key", accessCode);
                                    Log.i("access Code", accessCode);
                                    prefEdit.commit();
                                }
                            }
                        }

                    });

            alertDialog = builder.create();
            alertDialog.show();
        }

        lv = (ListView) findViewById(R.id.listview);
        al = new ArrayList<>();
        aa = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, al);
        al.add("Singapore's National Day is on 9 Aug");
        al.add("Singapore is 52 years old");
        al.add("Theme is: #OneNationTogether");
        lv.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action

        if (item.getItemId() == R.id.actionQuiz) {

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);
            RadioGroup rg1 = (RadioGroup) quiz
                    .findViewById(R.id.rgQuestionA);
            RadioGroup rg2 = (RadioGroup) quiz
                    .findViewById(R.id.rgQuestionB);
            RadioGroup rg3 = (RadioGroup) quiz
                    .findViewById(R.id.rgQuestionC);

            RadioButton selectedrb1 = (RadioButton)findViewById(rg1.getCheckedRadioButtonId());
            RadioButton selectedrb2 = (RadioButton)findViewById(rg2.getCheckedRadioButtonId());
            RadioButton selectedrb3 = (RadioButton)findViewById(rg3.getCheckedRadioButtonId());


            if (selectedrb1.getText().toString().equals("No")){
                score++;

                if (selectedrb2.getText().toString().equals("Yes")){
                    score++;

                    if(selectedrb3.getText().toString().equals("Yes")){
                        score++;
                    }
                }

            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

              builder.setView(quiz)
                    .setNegativeButton("Don't know lah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                           Toast.makeText(getBaseContext(), "Your score is: " + score, Toast.LENGTH_SHORT).show();
                        }

                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();









        } else if (item.getItemId() == R.id.actionSendToFriend) {

            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < al.size(); i++) {
                                items += al.get(i) + "\n";
                            }
                            if (which == 0) {
                                // Toast.makeText(MainActivity.this, "You said Monday",Toast.LENGTH_LONG).show();

                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{""});
                                email.putExtra(Intent.EXTRA_SUBJECT, "Know more about Singapore!");


                                email.putExtra(Intent.EXTRA_TEXT, items);

                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));
                            } else if (which == 1) {
//                                SmsManager smsManager = SmsManager.getDefault();
//                                smsManager.sendTextMessage("5556", null, items, null, null);

                                Intent sendSMS = new Intent(Intent.ACTION_VIEW);
                                sendSMS.setData(Uri.parse("sms:"));
                                sendSMS.putExtra("sms_body", items);
                                startActivity(sendSMS);
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        } else if (item.getItemId() == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You clicked yes",
                                    Toast.LENGTH_LONG).show();
                            finish();
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            final SharedPreferences.Editor prefEdit = prefs.edit();
                            prefEdit.putString("key", "");
                            prefEdit.commit();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "You clicked no",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
}
