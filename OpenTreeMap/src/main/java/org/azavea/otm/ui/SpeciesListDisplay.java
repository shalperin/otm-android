package org.azavea.otm.ui;


import java.util.LinkedHashMap;
import java.util.List;

import org.azavea.otm.App;
import org.azavea.otm.R;
import org.azavea.otm.FilterManager;
import org.azavea.otm.adapters.SpeciesAdapter;
import org.azavea.otm.data.Species;
import org.azavea.otm.fields.Field;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SpeciesListDisplay extends FilterableListDisplay<Species> {
    @Override
    protected int getFilterHintTextId() {
        return R.string.filter_species_hint;
    }

    @Override
    protected String getIntentDataKey() {
        return Field.TREE_SPECIES;
    }

    @Override
    public void onCreate(Bundle data) {
        super.onCreate(data);

        FilterManager search = App.getFilterManager();

        if (search.getSpecies().size() > 0) {
            renderSpeciesList();
        } else {
            search.loadSpeciesList(msg -> {
                if (msg.getData().getBoolean("success")) {
                    renderSpeciesList();
                } else {
                    Toast.makeText(App.getAppInstance(), "Could not get species list",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }
    }

    private void renderSpeciesList() {
        LinkedHashMap<Integer, Species> list = App.getFilterManager().getSpecies();

        Species[] species = list.values().toArray(new Species[list.size()]);

        // Sectionize by first letter of common name
        LinkedHashMap<CharSequence, List<Species>> speciesSections =
                groupListByKeyFirstLetter(species, Species::getCommonName);

        // Bind the custom adapter to the view
        SpeciesAdapter adapter = new SpeciesAdapter(this, speciesSections);
        Log.d(App.LOG_TAG, list.size() + " species loaded");

        renderList(adapter);
    }
}
