package dutta.swarnava.sundaysuspense;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recView;
    myadapter adapter;
    ArrayList<model> dataModels;
    JcPlayerView jcplayerView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    AlertDialog.Builder builder;
    public int counter;
    ArrayList<String> arrayListTitle = new ArrayList<>();
//    ArrayList<String> arrayListAuthor = new ArrayList<>();
    ArrayList<String> arrayListUrl = new ArrayList<>();
    ArrayList<String> arrayListIcon = new ArrayList<>();
    ArrayList<JcAudio> jcAudios = new ArrayList<>();


    int backButtonCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recView=(RecyclerView)findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Audios"), model.class)
                        .build();
        adapter= new myadapter(options);
        recView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new myadapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                jcplayerView.setVisibility(View.VISIBLE);
                jcplayerView.playAudio(jcAudios.get(position));
                jcplayerView.createNotification(R.drawable.ic_stat_name);

            }
        });
        dataModels = new ArrayList<>();
        builder = new AlertDialog.Builder(this);
        jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        setupToolbar();
        menuItemClick();
        retrieveSongs();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if (numberPicker.getValue()>=1 && numberPicker.getValue()<=60)
        {
            new CountDownTimer(numberPicker.getValue() * 60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    counter++;
                }

                @Override
                public void onFinish() {
                    MainActivity.this.finish();
                    System.exit(0);
                }
            }.start();
        }

    }

    private void retrieveSongs() {
        databaseReference=firebaseDatabase.getInstance().getReference("Audios");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    model Mod = dataSnapshot1.getValue(model.class);
                    arrayListTitle.add(Mod.gettitle());
//                    arrayListAuthor.add(Mod.getauthor());
                    arrayListUrl.add(Mod.geturl());
                    arrayListIcon.add(Mod.getimg());
                    jcAudios.add(JcAudio.createFromURL(Mod.gettitle(), Mod.geturl()));


//                    arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayListTitle) {
//                        @NonNull
//                        @Override
//                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                            View view = super.getView(position, convertView, parent);
//                            TextView textView = (TextView) findViewById(android.R.id.text1);
//
//                            return view;
//                        }
//                    };
                    jcplayerView.initPlaylist(jcAudios, null);
//                    L1.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);//Implement Hamburg Icon

        drawerLayout.addDrawerListener(toggle);//It takes the response of the Hamburger icon
        toggle.syncState();//It verifies whether DrawerLayout is Open or Close state

    }

    private void menuItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.share_with_friends:

                        break;
                    case R.id.timer:
                        showTimerAlert();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void showTimerAlert() {
            NumberPickerDialog newFragment = new NumberPickerDialog();
            newFragment.setValueChangeListener(this);
            newFragment.show(getSupportFragmentManager(), "time picker");
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            MainActivity.this.onRestart();

        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {

            case R.id.sortbynew:
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                break;
            case R.id.sortbytitle:
                FirebaseRecyclerOptions<model> optionstitle =
                        new FirebaseRecyclerOptions.Builder<model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Audios").orderByChild("title"), model.class)
                                .build();
                adapter= new myadapter(optionstitle);
                adapter.startListening();
                recView.setAdapter(adapter);
                break;
            case R.id.sortbyauthor:
                FirebaseRecyclerOptions<model> optionsauthor =
                        new FirebaseRecyclerOptions.Builder<model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Audios").orderByChild("author"), model.class)
                                .build();
                adapter= new myadapter(optionsauthor);
                adapter.startListening();
                recView.setAdapter(adapter);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void processsearch(String query) {
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Audios").orderByChild("title").startAt(query).endAt(query + "\uf8ff"), model.class)
                        .build();
        adapter= new myadapter(options);
        adapter.startListening();
        recView.setAdapter(adapter);
    }


}
