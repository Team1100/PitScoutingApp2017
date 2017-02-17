package team1100.pitscout2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class TeamList extends AppCompatActivity {

    private String[] teamList;
    public static final String TEAM_EXTRA = "arbitrary.arbitrary.value";

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openTeam(String team){
        Intent intent = new Intent(this,InfoPage.class);
        intent.putExtra(TEAM_EXTRA, team);
        startActivity(intent);
    }
}
