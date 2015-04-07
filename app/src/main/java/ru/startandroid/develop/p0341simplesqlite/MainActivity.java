package ru.startandroid.develop.p0341simplesqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etENword, etUAword, etID;

    DBHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnUpd = (Button) findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        etENword = (EditText) findViewById(R.id.etENword);
        etUAword = (EditText) findViewById(R.id.etUAword);
        etID = (EditText) findViewById(R.id.etID);

        // создаем объект для создания и управления версиями
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        // ������� ������ ��� ������
        ContentValues cvEN = new ContentValues();
        ContentValues cvUA = new ContentValues();

        // �������� ������ �� ����� �����
        String ENword = etENword.getText().toString();
        String UAword = etUAword.getText().toString();
        String id = etID.getText().toString();

        // ������������ � ��
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert  words: ---");
                // ���������� ������ ��� ������� � ���� ���: ������������ ������� - ��������

                cvEN.put("enword", ENword);
                cvUA.put("uaword", UAword);
                // ��������� ������ � �������� �� ID
                long enrowID = db.insert("enwords", null, cvEN);
//                long uarowID = db.insert("uawords", null, cvUA);
//                long enrowID = db.insert("enwords", null, cvEN);
//                long uarow = db.insert("uawords", "enwordFK", enrowID);

                try {


                    if (enrowID > 0) {    // write translation

                        db.execSQL("INSERT INTO uawords (uaword, enwordFK) VALUES('" + UAword + "','" + String.valueOf(enrowID) + "');");
                        db.execSQL("INSERT INTO enwords (enword, uawordFK) VALUES('" + ENword + "','" + String.valueOf(enrowID) + "');");
                    }



                    Log.d(LOG_TAG, "row inserted, enrowID = " + enrowID);
//                    Log.d(LOG_TAG, "row inserted, uarowID = " + uarowID);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case R.id.btnRead:
                Log.d(LOG_TAG, "--- Rows in enwords: ---");
                // ������ ������ ���� ������ �� ������� mytable, �������� Cursor
                Cursor c = db.query("enwords", null, null, null, null, null, null);

                // ������ ������� ������� �� ������ ������ �������
                // ���� � ������� ��� �����, �������� false
                if (c.moveToFirst()) {

                    // ���������� ������ �������� �� ����� � �������
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("enword");
                    int emailColIndex = c.getColumnIndex("uawordFK");


                    do {
                        // �������� �������� �� ������� �������� � ����� ��� � ���
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", enword = " + c.getString(nameColIndex) +
                                        ", uawordFK = " + c.getString(emailColIndex));
                        // ������� �� ��������� ������
                        // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");

                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // ������ ������ ���� ������ �� ������� mytable, �������� Cursor
                Cursor b = db.query("uawords", null, null, null, null, null, null);

                // ������ ������� ������� �� ������ ������ �������
                // ���� � ������� ��� �����, �������� false
                if (b.moveToFirst()) {

                    // ���������� ������ �������� �� ����� � �������
                    int idColIndex = b.getColumnIndex("id");
                    int nameColIndex = b.getColumnIndex("uaword");
                    int emailColIndex = b.getColumnIndex("enwordFK");


                    do {
                        // �������� �������� �� ������� �������� � ����� ��� � ���
                        Log.d(LOG_TAG,
                                "ID = " + b.getInt(idColIndex) +
                                        ", uaword = " + b.getString(nameColIndex) +
                                        ", enwordFK = " + b.getString(emailColIndex));
                        // ������� �� ��������� ������
                        // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
                    } while (b.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                b.close();
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // ������� ��� ������
                int clearCountUA = db.delete("uawords", null, null);
                Log.d(LOG_TAG, "uawords deleted rows count = " + clearCountUA);
                int clearCountEN = db.delete("enwords", null, null);
                Log.d(LOG_TAG, "enwords deleted rows count = " + clearCountEN);
                break;

      /*      case R.id.btnUpd:
               if (id.equalsIgnoreCase("")) {
                break;
              }

              Log.d(LOG_TAG, "--- Update mytabe: ---");
              // ���������� �������� ��� ����������
             cv.put("name", name);
              cv.put("email", email);
              // ��������� �� id
              int updCount = db.update("mytable", cv, "id = ?",
                  new String[] { id });

              Log.d(LOG_TAG, "updated rows count = " + updCount);
              break;
            case R.id.btnDel:
              if (id.equalsIgnoreCase("")) {
                break;
              }

              Log.d(LOG_TAG, "--- Delete from mytabe: ---");
              // ������� �� id
              int delCount = db.delete("mytable", "id = " + id, null);
              Log.d(LOG_TAG, "deleted rows count = " + delCount);
              break;
          }*/
                // ��������� ����������� � ��
                //dbHelper.close();
        } // switch (v.getId()) {


    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // ����������� �����������
            super(context, "wordDB", null, 1);
            Log.d(LOG_TAG, "---  public DBHelper(Context context) ---");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // creating tables if they aren't existing

            // creating table with english words
            db.execSQL("create table enwords ("
                    + "id integer primary key autoincrement,"
                    + "enword, "
                    + "uawordFK" + ");");

            // creating table with ukrainian words
            db.execSQL("create table uawords ("
                    + "id integer primary key autoincrement,"
                    + "uaword, "
                    + "enwordFK" + ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
