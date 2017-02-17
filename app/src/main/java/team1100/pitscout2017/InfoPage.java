package team1100.pitscout2017;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private final int NAME_INDEX = 0;
    private final int DRIVE_INDEX = 1;
    private final int CIM_INDEX = 2;
    private final int INTAKE_INDEX = 3;
    private final int SHOOTER_INDEX = 4;
    private final int CLIMB_INDEX = 5;
    private final int COMMENT_INDEX = 6;


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
        retreiveSheet();
    }
    @Override
    public void onStart(){
        super.onStart();
        retreiveSheet();
    }

    public void retreiveSheet(){
        List<String> infos = readInfo();
        if(infos.size()>0){
            String name = infos.get(NAME_INDEX);
            String comment = infos.get(COMMENT_INDEX);
            boolean climb = Boolean.parseBoolean(infos.get(CLIMB_INDEX));
            int cim = Integer.parseInt(infos.get(CIM_INDEX));
            int drive = Integer.parseInt(infos.get(DRIVE_INDEX));
            int shoot = Integer.parseInt(infos.get(SHOOTER_INDEX));
            int intake = Integer.parseInt(infos.get(INTAKE_INDEX));

            EditText nameBox = (EditText)findViewById(R.id.team_name);
            EditText commentBox = (EditText)findViewById(R.id.comments);
            CheckBox climbBox = (CheckBox)findViewById(R.id.climber);
            Spinner cimSpin = (Spinner)findViewById(R.id.cim_count);
            Spinner driveSpin = (Spinner)findViewById(R.id.drive_type);
            Spinner shootSpin = (Spinner)findViewById(R.id.shoot_position);
            Spinner intakeSpin = (Spinner)findViewById(R.id.intake_type);

            nameBox.setText(name);
            commentBox.setText(comment);
            climbBox.setChecked(climb);
            cimSpin.setSelection(cim);
            driveSpin.setSelection(drive);
            shootSpin.setSelection(shoot);
            intakeSpin.setSelection(intake);
        }
    }

    public void writeInfo(){
        String filename = teamNumber;
        FileOutputStream outputStream;
        String[] infos = new String[7];

        infos[NAME_INDEX] = ((EditText)findViewById(R.id.team_name)).getText().toString();
        infos[CIM_INDEX] = Integer.toString(((Spinner)findViewById(R.id.cim_count)).getSelectedItemPosition());
        infos[DRIVE_INDEX] = Integer.toString(((Spinner)findViewById(R.id.drive_type)).getSelectedItemPosition());
        infos[SHOOTER_INDEX] = Integer.toString(((Spinner)findViewById(R.id.shoot_position)).getSelectedItemPosition());
        infos[INTAKE_INDEX] = Integer.toString(((Spinner)findViewById(R.id.intake_type)).getSelectedItemPosition());
        infos[CLIMB_INDEX] = String.valueOf(((CheckBox)findViewById(R.id.climber)).isChecked());
        infos[COMMENT_INDEX] = ((EditText)findViewById(R.id.comments)).getText().toString().replace(System.getProperty("line.separator"), "");

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