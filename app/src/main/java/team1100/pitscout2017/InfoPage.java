package team1100.pitscout2017;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InfoPage extends AppCompatActivity {

    private String teamNumber;
    public static final String TEAM_NUMBER_EXTRA = "team.number.frenchfries.cantstopwontstop";

    public static final int NAME_INDEX = 0;
    public static final int DRIVE_INDEX = 1;
    public static final int CIM_INDEX = 2;
    public static final int INTAKE_INDEX = 3;
    public static final int SHOOTER_INDEX = 4;
    public static final int CLIMB_INDEX = 5;
    public static final int GEAR_INDEX = 6;
    public static final int COMMENT_INDEX = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);

        Intent intent = this.getIntent();
        teamNumber = intent.getStringExtra(TeamList.TEAM_EXTRA);
        TextView number = (TextView)findViewById(R.id.team_number_display);
        number.setText(teamNumber);

        Spinner driveSpinner = (Spinner) findViewById(R.id.drive_type);
        ArrayAdapter<CharSequence> driveAdapter = ArrayAdapter.createFromResource(this,
                R.array.options_drive, R.layout.spinner_item);
        driveSpinner.setAdapter(driveAdapter);

        Spinner cimSpinner = (Spinner) findViewById(R.id.cim_count);
        ArrayAdapter<CharSequence> cimAdapter = ArrayAdapter.createFromResource(this,
                R.array.options_cim, R.layout.spinner_item);
        cimSpinner.setAdapter(cimAdapter);

        Spinner intakeSpinner = (Spinner) findViewById(R.id.intake_type);
        ArrayAdapter<CharSequence> intakeAdapter = ArrayAdapter.createFromResource(this,
                R.array.options_intake, R.layout.spinner_item);
        intakeSpinner.setAdapter(intakeAdapter);

        Spinner shootSpinner = (Spinner) findViewById(R.id.shoot_position);
        ArrayAdapter<CharSequence> shootAdapter = ArrayAdapter.createFromResource(this,
                R.array.options_shoot, R.layout.spinner_item);
        shootSpinner.setAdapter(shootAdapter);

        Spinner gearSpinner = (Spinner)findViewById(R.id.gear_ability);
        ArrayAdapter<CharSequence> gearAdapter = ArrayAdapter.createFromResource(this,
                R.array.options_gear, R.layout.spinner_item);
        gearSpinner.setAdapter(gearAdapter);

    }

    @Override
    public void onPause(){
        super.onPause();
        writeInfo();
    }

    @Override
    public void onStop(){
        super.onStop();
        writeInfo();
    }

    @Override
    public void onResume(){
        super.onResume();
        retrieveSheet();
    }
    @Override
    public void onStart(){
        super.onStart();
        retrieveSheet();
    }

    public void retrieveSheet(){
        List<String> infos = readInfo();
        if(infos.size()>=8){
            String name = infos.get(NAME_INDEX);
            String comment = infos.get(COMMENT_INDEX);
            boolean climb = Boolean.parseBoolean(infos.get(CLIMB_INDEX));
            int cim = Integer.parseInt(infos.get(CIM_INDEX));
            int drive = Integer.parseInt(infos.get(DRIVE_INDEX));
            int shoot = Integer.parseInt(infos.get(SHOOTER_INDEX));
            int intake = Integer.parseInt(infos.get(INTAKE_INDEX));
            int gear  = Integer.parseInt(infos.get(GEAR_INDEX));

            EditText nameBox = (EditText)findViewById(R.id.team_name);
            EditText commentBox = (EditText)findViewById(R.id.comments);
            CheckBox climbBox = (CheckBox)findViewById(R.id.climber);
            Spinner cimSpin = (Spinner)findViewById(R.id.cim_count);
            Spinner driveSpin = (Spinner)findViewById(R.id.drive_type);
            Spinner shootSpin = (Spinner)findViewById(R.id.shoot_position);
            Spinner intakeSpin = (Spinner)findViewById(R.id.intake_type);
            Spinner gearSpin = (Spinner)findViewById(R.id.gear_ability);

            nameBox.setText(name);
            commentBox.setText(comment);
            climbBox.setChecked(climb);
            cimSpin.setSelection(cim);
            driveSpin.setSelection(drive);
            shootSpin.setSelection(shoot);
            intakeSpin.setSelection(intake);
            gearSpin.setSelection(gear);
        }
    }

    public void writeInfo(){
        String filename = teamNumber;
        FileOutputStream outputStream;
        String[] infos = new String[7];

        infos[NAME_INDEX] = ((EditText)findViewById(R.id.team_name)).getText().toString().replace(","," ");
        infos[CIM_INDEX] = Integer.toString(((Spinner)findViewById(R.id.cim_count)).getSelectedItemPosition());
        infos[DRIVE_INDEX] = Integer.toString(((Spinner)findViewById(R.id.drive_type)).getSelectedItemPosition());
        infos[SHOOTER_INDEX] = Integer.toString(((Spinner)findViewById(R.id.shoot_position)).getSelectedItemPosition());
        infos[INTAKE_INDEX] = Integer.toString(((Spinner)findViewById(R.id.intake_type)).getSelectedItemPosition());
        infos[CLIMB_INDEX] = String.valueOf(((CheckBox)findViewById(R.id.climber)).isChecked());
        infos[GEAR_INDEX] = Integer.toString(((Spinner)findViewById(R.id.gear_ability)).getSelectedItemPosition());
        infos[COMMENT_INDEX] = ((EditText)findViewById(R.id.comments)).getText().toString().replace(System.getProperty("line.separator"), "").replace(","," ");

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            for(String info: infos){
                outputStream.write((info+"\n").getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> readInfo(){
        String filename = teamNumber;
        List<String> data = new ArrayList<>();
        try{
            InputStream inputStream = getBaseContext().openFileInput(filename);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                System.out.println("About to read");
                while ((line=bufferedReader.readLine())!=null){
                    System.out.println("Adding data: " + line);
                    data.add(line);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public void openPhotos(View view){
        Intent intent = new Intent(this,PhotoPage.class);
        intent.putExtra(TEAM_NUMBER_EXTRA,teamNumber);
        startActivity(intent);
    }

    @Deprecated //Angrilly
    public List<String> getInfo(){
        try{

            List<String> infos = new ArrayList<>();

            EditText nameBox = (EditText)findViewById(R.id.team_name);
            EditText commentBox = (EditText)findViewById(R.id.comments);
            CheckBox climbBox = (CheckBox)findViewById(R.id.climber);
            Spinner cimSpin = (Spinner)findViewById(R.id.cim_count);
            Spinner driveSpin = (Spinner)findViewById(R.id.drive_type);
            Spinner shootSpin = (Spinner)findViewById(R.id.shoot_position);
            Spinner intakeSpin = (Spinner)findViewById(R.id.intake_type);

            infos.add(nameBox.getText().toString());
            infos.add(Integer.toString(driveSpin.getSelectedItemPosition()));
            infos.add(Integer.toString(cimSpin.getSelectedItemPosition()));
            infos.add(Integer.toString(intakeSpin.getSelectedItemPosition()));
            infos.add(Integer.toString(shootSpin.getSelectedItemPosition()));
            infos.add(Boolean.toString(climbBox.isChecked()));
            infos.add(commentBox.getText().toString());

            return infos;
        }catch(Exception e){
            return readInfo();
        }
    }
}