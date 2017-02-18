package team1100.pitscout2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TeamList extends AppCompatActivity {

    private String[] teamList;
    public static final String TEAM_EXTRA = "arbitrary.arbitrary.value";
    public static final String EXTRA_BLUE_DATA = "norse.viking.harold.connect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        teamList = intent.getStringArrayExtra(LaunchActivity.TEAMS_EXTRA);

        setContentView(R.layout.activity_team_list);

        LinearLayout list = (LinearLayout)findViewById(R.id.team_scroller);
        list.removeAllViews();
        for(String team : teamList){
            Button teamButton = new Button(this);
            teamButton.setText(team);
            teamButton.setTextSize(12);
            teamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTeam(((Button)v).getText().toString());
                }
            });
            list.addView(teamButton);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void sendData(){
        String[] infos = new String[teamList.length];
        for(int i = 0; i<infos.length;i++){
            String filename = teamList[i];
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
            try{
                String info = teamList[i]+",";
                info += data.get(InfoPage.NAME_INDEX)+",";
                switch (Integer.parseInt(data.get(InfoPage.CIM_INDEX))){
                    case 0:
                        info+="-,";
                        break;
                    case 1:
                        info+="2,";
                        break;
                    case 2:
                        info+="4,";
                        break;
                    case 4:
                        info+="6,";
                        break;
                    case 5:
                        info+="8,";
                        break;
                    default:
                        info+="-,";
                        break;
                }
                switch (Integer.parseInt(data.get(InfoPage.DRIVE_INDEX))){
                    case 0:
                        info+="-,";
                        break;
                    case 1:
                        info+="Tank,";
                        break;
                    case 2:
                        info+="Mecanum,";
                        break;
                    case 3:
                        info+="Swerve,";
                }
                switch (Integer.parseInt(data.get(InfoPage.INTAKE_INDEX))){
                    case 0:
                        info+="-,";
                        break;
                    case 1:
                        info+="Floor,";
                        break;
                    case 2:
                        info+="Hopper,";
                        break;
                    case 3:
                        info+="Both,";
                        break;
                }
                switch (Integer.parseInt(data.get(InfoPage.SHOOTER_INDEX))){
                    case 0:
                        info += "-,";
                        break;
                    case 1:
                        info +="None,";
                        break;
                    case 2:
                        info+="Boiler,";
                        break;
                    case 3:
                        info+="Hopper,";
                        break;
                    case 4:
                        info+="Airship,";
                        break;
                    case 5:
                        info+="Variable,";
                        break;
                }
                info+=data.get(InfoPage.COMMENT_INDEX);
                infos[i] = info;
            }catch(Exception q){
            }
        }

        Intent intent = new Intent(this,BluetoothActivity.class);
        intent.putExtra(EXTRA_BLUE_DATA,infos);
        startActivity(intent);
    }
    public void openTeam(String team){
        Intent intent = new Intent(this,InfoPage.class);
        intent.putExtra(TEAM_EXTRA, team);
        startActivity(intent);
    }
}