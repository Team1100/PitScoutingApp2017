package team1100.pitscout2017;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class LaunchActivity extends AppCompatActivity {

    public static final String TEAMS_EXTRA = "com.list.android.swagger";

    private final String[] graniteState = ("78,95,31,138,238,319,467,509,811,885," +
            "1058,1073,1100,1247,1289,1307,1512,1517,1729,1831,2084,2876,3323,3467,3566" +
            ",3958,4473,4908,4929,5422,5459,5506,5687,5735,5902,6172,6324,6328,6763").split(",");
    private final String[] rhodeIsland = ("69,78,88,121,125,126,157,176,190,467,1099,1100," +
            "1124,1153,1277,1350,1757,1768,1786,1973,2064,2079,2168,2262,2877,3466,3525,3719,3780," +
            "4048,4097,4151,4176,4796,5000,5112,5846,5856,6617,6620,6731").split(",");
    private final String[] neChamp = null;
    private final String[] worldChamp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void startScouting(View view){
        RadioGroup radio = (RadioGroup) findViewById(R.id.event_selector_radio);
        String[] teamlist;
        switch (radio.getCheckedRadioButtonId()){
            case 0:
                teamlist = graniteState;
                break;
            case 1:
                teamlist = rhodeIsland;
                break;
            case 2:
                teamlist = neChamp;
                break;
            case 3:
                teamlist = worldChamp;
                break;
            default:
                teamlist = null;
        }
        Intent intent = new Intent(this,TeamList.class);
        intent.putExtra(TEAMS_EXTRA, teamlist);
        startActivity(intent);
    }

}
