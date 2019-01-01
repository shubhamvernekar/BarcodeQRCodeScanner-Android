package com.example.shubham.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LinearGradient;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class SheetsPage extends AppCompatActivity {

    private String lab,shID,shName;
    Button but;
    ListView lv;
    String filename = "BCScannet";
    SQLiteDatabase sqLiteDatabase;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> theList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets_page);
        but = (Button)findViewById(R.id.insertButton);
        lv = (ListView)findViewById(R.id.list);
        lv.setClickable(true);
        theList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
        sqLiteDatabase  = openOrCreateDatabase("BCScannerDB",Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SheetData(Id Integer Primary Key Autoincrement,Label VARCHAR(255),SheetID VARCHAR(255),SheetName VARCHAR(255)); ");

        displayDataToList();


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                try{
                 String data = arrayAdapter.getItem(pos);
                int i = data.indexOf("-");
                data= data.substring(0,i);
                int idd = Integer.parseInt(data);
                sqLiteDatabase.delete("SheetData","Id"+"="+idd,null);
                theList.remove(pos);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(SheetsPage.this, "Item Deleted", Toast.LENGTH_LONG).show();
               }
                catch (Exception e){
                    Toast.makeText(SheetsPage.this,""+e,Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c;
                String SheetId="";
                String SheetName="";
                try {
                    String data = arrayAdapter.getItem(position);
                    int i = data.indexOf("-");
                    data = data.substring(0, i);
                    int idd = Integer.parseInt(data);
                    c = sqLiteDatabase.rawQuery("SELECT * FROM SheetData WHERE Id = ?", new String[]{ ""+idd });
                    if(c!=null && c.moveToFirst()){
                        SheetId = c.getString(c.getColumnIndex("SheetID"));
                        SheetName = c.getString(c.getColumnIndex("SheetName"));
                    }
                    Toast.makeText(SheetsPage.this,SheetId,Toast.LENGTH_LONG).show();
                    Intent in = new Intent(SheetsPage.this, OffliceMode.class);
                    in.putExtra("arg1",SheetId);
                    in.putExtra("arg2",SheetName);
                    startActivity(in);
                } catch (Exception e) {
                    Toast.makeText(SheetsPage.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    public void insert(View view){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText label = new EditText(this);
        label.setHint("Enter Label");
        layout.addView(label);
        final EditText sheetID = new EditText(this);
        sheetID.setHint("Enter Google sheet id from URL");
        layout.addView(sheetID);
        final EditText sheetName = new EditText(this);
        sheetName.setHint("Enter name of sheet");
        layout.addView(sheetName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insert values");
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lab = label.getText().toString();
                shID = sheetID.getText().toString();
                shName = sheetName.getText().toString();
                String data = lab+"\t"+shID+"\t"+shName;
                try{
                    sqLiteDatabase.execSQL("INSERT INTO SheetData(Label,SheetID,SheetName)VALUES('"+lab+"','"+shID+"','"+shName+"');");
                    Toast.makeText(SheetsPage.this,"Record Saved Successfully!",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(SheetsPage.this,""+e,Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }


    public void displayDataToList(){
        Cursor c= sqLiteDatabase.rawQuery("Select * from SheetData",null);
        if(c.getCount()<= 0){
            Toast.makeText(SheetsPage.this,"No Record present insert Records",Toast.LENGTH_LONG).show();
        }
        else{
            while (c.moveToNext()){
                String s = c.getString(c.getColumnIndex("Id"))+"-"+c.getString(c.getColumnIndex("Label"));
                theList.add(s);
                lv.setAdapter(arrayAdapter);
            }
        }
            c.close();
    }


    public void refresh(View view) {
        arrayAdapter.clear();
        displayDataToList();
    }
}
