package djpiper28.mtgnewsapp.cardpreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import djpiper28.mtgnewsapp.R;
import djpiper28.settings.SettingsLoader;
import forohfor.scryfall.api.Card;
import forohfor.scryfall.api.MTGCardQuery;
import forohfor.scryfall.api.Set;

public class CardPreviewHostActivity extends AppCompatActivity {

    public static Set set;
    public static List<Card> cards;
    public static List<Card> cardsUnfiltered;

    private final String[] sortTypes = {"CMC", "Name", "Power", "Toughness", "Collectors Number"};
    private final String[] colours = {"Any", "W", "U", "B", "R", "G"};
    private final String[] superTypes = {"Any", "Land", "Creature", "Artifact", "Snow", "Enchantment", "Instant", "Sorcery", "Legendary", "Basic"};

    private CardPreviewHostRecycleViewer adapter;

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_browser_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card_preview_host);

        cardsUnfiltered = clone(cards);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(SettingsLoader.getSettingsLoader().getSettings().getPrimaryColour());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Card showing stuff
        this.adapter = new CardPreviewHostRecycleViewer(this, cards);
        RecyclerView recycleViewer = findViewById(R.id.card_preview_recycle_viewer);
        recycleViewer.setLayoutManager(new LinearLayoutManager(this));
        recycleViewer.setAdapter(adapter);

        // Search stuff
        Spinner sortTypeSpinner = (Spinner) findViewById(R.id.sort_type);
        ArrayAdapter<String> sortSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortTypes);
        sortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortTypeSpinner.setAdapter(sortSpinnerAdapter);

        Spinner colourTypeSpinner = (Spinner) findViewById(R.id.colour_type);
        ArrayAdapter<String> colourSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colours);
        colourSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colourTypeSpinner.setAdapter(colourSpinnerAdapter);

        Spinner superTypeSpinner = (Spinner) findViewById(R.id.super_type_spinner);
        ArrayAdapter<String> superSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, superTypes);
        superSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        superTypeSpinner.setAdapter(superSpinnerAdapter);

        // Add listeners
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        SearchView searchView = findViewById(R.id.card_search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters();
                return false;
            }
        });

        superTypeSpinner.setOnItemSelectedListener(listener);
        colourTypeSpinner.setOnItemSelectedListener(listener);
        sortTypeSpinner.setOnItemSelectedListener(listener);
    }

    private SortBy getSortType() {
        switch (((Spinner) findViewById(R.id.sort_type)).getSelectedItem().toString().toLowerCase()) {

            case "cmc":
                return SortBy.CMC;

            case "name":
                return SortBy.NAME;

            case "power":
                return SortBy.POWER;

            case "toughness":
                return SortBy.TOUGHNESS;

            case "collectors number":
                return SortBy.COLLECTORS_NUMBER;

            default:
                return SortBy.COLLECTORS_NUMBER;
        }
    }

    private void applyFilters() {
         updateCardList(CardSearchAndSorter.search(cardsUnfiltered, ((SearchView) findViewById(R.id.card_search_view)).getQuery().toString(),
                 ((Spinner) findViewById(R.id.super_type_spinner)).getSelectedItem().toString(),
                 ((Spinner) findViewById(R.id.colour_type)).getSelectedItem().toString(),
                 getSortType(), ((Switch) findViewById(R.id.ascending_order)).isChecked()));
    }

    private List<Card> clone(List<Card> cards) {
        List<Card> output = new LinkedList<>();

        for (Card card: cards) {
            output.add(card);
        }

        return output;
    }

    /*
     * Checks if the card list has changed and if it has it then refreshes the adapter
     */
    private void updateCardList(List<Card> newCards) {
        if(!newCards.equals(cards)) {
            // Not optimal but good enough

            while(!cards.isEmpty()) {
                cards.remove(0);
            }

            cards.addAll(newCards);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                updateCardList(MTGCardQuery.search("set:" + set.getCode()));
                cardsUnfiltered = clone(cards);
                return true;

            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this new MTG set "+ set.getName() +"!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this new set! " + set.getScryfallURI());
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                return true;

            case R.id.action_open:
                openLink(set.getScryfallURI());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
