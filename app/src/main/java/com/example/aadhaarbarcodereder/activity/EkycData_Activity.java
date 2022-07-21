package com.example.aadhaarbarcodereder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aadhaarbarcodereder.R;

public class EkycData_Activity extends AppCompatActivity {
    ImageView userprofile;
    LinearLayout mainlayout;
    TextView Aadhaarno, Transactionid, Username, UserDob, Gender, MobileNo, Email, CareOf, Address, Pincode;
    private String strtransactionids, struid, strvillage, strstreet, strstate, strpincode, strmilestone, strlocation, strroad, strlandmark, strhouse, strdist, strfithername, strphoto, strphoneno, strname, strmss, strgender, stremail = "null", strdob,strpdf;
    private String straddress;
    byte[] Bytphoto;
    String Aadhaarpdf;
    Bitmap aadhaarPDFBit;
    String UUIDNo;
    boolean boolean_save;
    Button sharebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekyc_data);

        strtransactionids = getIntent().getStringExtra("strTransactionid");
        struid = getIntent().getStringExtra("UID");
        UUIDNo = getIntent().getStringExtra("UUID");
        strvillage = getIntent().getStringExtra("vtc");
        strstreet = getIntent().getStringExtra("street");
        strstate = getIntent().getStringExtra("state");
        strpincode = getIntent().getStringExtra("pc");
        strmilestone = getIntent().getStringExtra("ms");
        strlocation = getIntent().getStringExtra("loc");
        strlandmark = getIntent().getStringExtra("lm");
        strhouse = getIntent().getStringExtra("house");
        strdist = getIntent().getStringExtra("dist");
        strfithername = getIntent().getStringExtra("co");
        strphoneno = getIntent().getStringExtra("phone");
        strname = getIntent().getStringExtra("name");
        strmss = getIntent().getStringExtra("mss");
        strgender = getIntent().getStringExtra("gender");
        stremail = getIntent().getStringExtra("email");
        strdob = getIntent().getStringExtra("dob");
        strphoto = getIntent().getStringExtra("photo");
        Bytphoto = getIntent().getByteArrayExtra("imageBYte");
        //    Aadhaarpdf = getIntent().getStringExtra("PDFpic");
        straddress = strhouse + " " + strmilestone + " " + strvillage + " " + strstreet + " " + strlandmark + " " + strlocation + " " + strdist + " " + strstate;
        init();
        setdeta();
    }
    private void setdeta() {
        Transactionid.setText(strtransactionids);
        Aadhaarno.setText(struid+UUIDNo);
        Username.setText(strname);
        Gender.setText(strgender);
        UserDob.setText(strdob);
        CareOf.setText(strfithername);
        Address.setText(straddress);
        // pincode.setText(strpincode);
        if (strphoneno.equals("")) {
            MobileNo.setText("N/A");
        } else {
            MobileNo.setText(strphoneno);
        }
        MobileNo.setText(strphoneno);
        if (stremail.equals("null")) {
            Email.setText("N/A");
        } else {
            Email.setText(stremail);
        }
        //  byte[] imageAsBytes = Base64.decode(strphoto.getBytes(), Base64.DEFAULT);
        userprofile.setImageBitmap(BitmapFactory.decodeByteArray(Bytphoto, 0, Bytphoto.length));
        Pincode.setText(strpincode);
    }

    private void init() {
        userprofile = (ImageView) findViewById(R.id.iv_User);
        Aadhaarno = (TextView) findViewById(R.id.tv_aadhaarno);
        Transactionid = (TextView) findViewById(R.id.tv_aadhaartransactionid);
        Username = (TextView) findViewById(R.id.tv_aadhaarname);
        UserDob = (TextView) findViewById(R.id.tv_aadhaardob);
        Gender = (TextView) findViewById(R.id.tv_aadhaargender);
        MobileNo = (TextView) findViewById(R.id.tv_aadhaarphonenumber);
        Email = (TextView) findViewById(R.id.tv_aadhaaremail);
        CareOf = (TextView) findViewById(R.id.tv_aadhaarcastof);
        Address = (TextView) findViewById(R.id.tv_aadhaaraddress);
        Pincode = (TextView) findViewById(R.id.tv_pincode);
    }
}