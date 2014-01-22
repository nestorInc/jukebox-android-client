package net.szchmop.jbclient;
/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;


/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        menu.findItem(R.id.action_websearch).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_settings:
                System.out.println("Settings");
                /*
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SettingsFragment())
                        .commit();

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (position == 0)
        {
            // update the main content by replacing fragments
            Fragment fragment = new PlanetFragment();
            Bundle args = new Bundle();
            args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(mPlanetTitles[position]);
        }
        else if (position == 1)
        {
            // update the main content by replacing fragments
            Fragment fragment = new SettingsFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(mPlanetTitles[position]);
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment implements JukeboxClientListener, JukeboxPlayerListener, SharedPreferences.OnSharedPreferenceChangeListener {

        public static final String ARG_PLANET_NUMBER = "planet_number";
        private JukeboxClient m_JukeboxClient;
        private JukeboxPlayer m_JukeboxPlayer;

        private Button m_PlayButton;
        private Button m_ReloadButton;
        private Button m_NextButton;

        private TextView m_StatusText;
        private TextView m_CurrentSongText;
        private TextView m_PlayQueueText;

        private boolean m_ConfigChanged;

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SetupStuff();
            m_ConfigChanged = false;
        }

        @Override
        public void onResume() {
            super.onResume();
            if (m_ConfigChanged == true) {
                System.out.println("Waking up - preferences changed!");
                SetupStuff();
            }
        }

        private void SetupStuff()
        {
            if (m_JukeboxClient != null) {
                m_JukeboxClient.removeJukeboxClientListener(this);
                m_JukeboxClient = null;
            }
            if (m_JukeboxPlayer != null) {
                m_JukeboxPlayer.Stop();
                m_JukeboxPlayer.removeJukeboxClientListener(this);
                m_JukeboxPlayer = null;
            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String host = sharedPreferences.getString(SettingsFragment.KEY_PREFS_SERVER_URL, "");
            int port = Integer.parseInt(sharedPreferences.getString(SettingsFragment.KEY_PREFS_SERVER_PORT, "80"));
            String login = sharedPreferences.getString(SettingsFragment.KEY_PREFS_AUTH_LOGIN, "");
            String password = sharedPreferences.getString(SettingsFragment.KEY_PREFS_AUTH_PASSWORD, "");

            m_JukeboxClient = new JukeboxClient(host, port, login, password);
            m_JukeboxClient.addJukeboxClientListener(this);

            m_JukeboxPlayer = new JukeboxPlayer(host, port, login, password);
            m_JukeboxPlayer.addJukeboxClientListener(this);
        }

        private void Next () {

            if (m_StatusText != null)
                m_StatusText.setText("Please wait...");
            m_JukeboxClient.Next();
        }

        private void Play () {
            if (m_JukeboxPlayer.getState() == PlayerState.STOPPED)
                m_JukeboxPlayer.Play();
            else
                m_JukeboxPlayer.Stop();
        }

        private void Reload () {
            m_JukeboxClient.Reload();
        }

        private View.OnClickListener nextOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Next button clicked.");
                Next();
            }
        };

        private View.OnClickListener playOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Play button clicked.");
                Play();
            }
        };

        private View.OnClickListener reloadOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Reload button clicked.");
                Reload();
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            if (i == 0) {
                View rootView = inflater.inflate(R.layout.fragment_player, container, false);
                m_NextButton = (Button) rootView.findViewById(R.id.nextButton);
                if (m_NextButton != null) {
                    m_NextButton.setOnClickListener(nextOnClickListener);
                }
                m_PlayButton = (Button) rootView.findViewById(R.id.playButton);
                if (m_PlayButton != null) {
                    m_PlayButton.setOnClickListener(playOnClickListener);
                }
                m_ReloadButton = (Button) rootView.findViewById(R.id.reloadButton);
                if (m_ReloadButton != null) {
                    m_ReloadButton.setOnClickListener(reloadOnClickListener);
                }
                m_StatusText = (TextView) rootView.findViewById(R.id.text);
                m_CurrentSongText = (TextView) rootView.findViewById(R.id.currentSong);
                m_PlayQueueText = (TextView) rootView.findViewById(R.id.playQueue);
                getActivity().setTitle(R.string.player_title);
                return rootView;

            } else {
                View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
                String planet = getResources().getStringArray(R.array.planets_array)[i];

                int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                        "drawable", getActivity().getPackageName());
                ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
                getActivity().setTitle(planet);
                return rootView;
            }

        }

        @Override
        public void requestSuccess() {
            if (m_StatusText != null)
                m_StatusText.setText("Success!");
            if (m_PlayQueueText != null)
                m_CurrentSongText.setText(m_JukeboxClient.m_Channel.current_song.artist + " - " + m_JukeboxClient.m_Channel.current_song.title);

            if (m_PlayQueueText != null)
            {
                String text = "";

                for (int i = 0; i < m_JukeboxClient.m_Channel.play_queue.size(); i++)
                {
                    JukeboxClient.Song song = m_JukeboxClient.m_Channel.play_queue.get(i);
                    text += song.artist + " - " + song.title + "\n";
                }

                m_PlayQueueText.setText(text);
            }
        }

        @Override
        public void requestFailure(Exception e) {
            if (m_StatusText != null)
                m_StatusText.setText("error: " + e.getMessage());
        }

        @Override
        public void onPlayerStatusChanged(PlayerState state) {
            switch (state) {
                case CONNECTING:
                    m_PlayButton.setEnabled(false);
                    m_PlayButton.setText("Connecting...");
                    break;
                case PLAYING:
                    m_PlayButton.setEnabled(true);
                    m_PlayButton.setText("Stop");
                    break;
                default:
                    m_PlayButton.setEnabled(true);
                    m_PlayButton.setText("Play");
                    break;
            }
        }

        @Override
        public void playerBusy(Boolean busy) {

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            System.out.println("Preferences changed. Will have to reset stuff.");
            m_ConfigChanged = true;
        }
    }
}