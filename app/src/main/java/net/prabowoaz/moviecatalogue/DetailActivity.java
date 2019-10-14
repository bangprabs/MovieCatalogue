package net.prabowoaz.moviecatalogue;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.prabowoaz.moviecatalogue.database.DatabaseHelper;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL = "extra_detail";
    private TextView titleFilm, descFilm, realesseMovie;
    private ImageView posterMovie;
    private Menu menu;
    private Film film;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeView();

        databaseHelper = new DatabaseHelper(this);

        film = getIntent().getParcelableExtra(EXTRA_DETAIL);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(film.getTitle() != null ? film.getTitle() : film.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        titleFilm.setText(film.getTitle() != null ? film.getTitle() : film.getName());
        descFilm.setText(film.getOverview());
        realesseMovie.setText(film.getRelease_date() != null ? film.getRelease_date() : film.getFirst_air_date());

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + film.getPoster_path()).into(posterMovie);
    }

    private void initializeView() {
        titleFilm = findViewById(R.id.titleMovie);
        descFilm = findViewById(R.id.descMovie);
        posterMovie = findViewById(R.id.posterMovie);
        realesseMovie = findViewById(R.id.releaseDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.favorite:
                addToFavorite();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite() {
        if (databaseHelper.getFilm(film.getId(), film.getTitle() != null) == null) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white));
            databaseHelper.insertFilm(film);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.addFavorite), Toast.LENGTH_SHORT).show();
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
            databaseHelper.deleteFilm(film.getId(), film.getTitle() != null);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.removeFavorite), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        menu.getItem(0).setIcon(databaseHelper.getFilm(film.getId(), film.getTitle() != null) != null ?
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_white) :
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));

        return super.onCreateOptionsMenu(menu);
    }
}
