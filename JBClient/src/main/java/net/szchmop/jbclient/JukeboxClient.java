package net.szchmop.jbclient;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by beuiot on 23/09/13.
 */
public class JukeboxClient {
    public class Song {
        public int mid;
        public String title;
        public String artist;
        public String album;
        public int year;
        public int track;
        public int trackNb;
        public int genre;
        public int duration;
    }

    public class Channel {
        public Song current_song;
        public int elapsed;
        public int listener_count;
        LinkedList<Song> play_queue;
    }


    private final String s_JsonApiPath = "api/json";

    private String m_Host;
    private int m_Port;
    private String m_Login;
    private String m_Password;

    private DefaultHttpClient httpClient;

    public Channel m_Channel;
    private int m_Timestamp;

    //LISTENER STUFF
    private LinkedList<JukeboxClientListener> clientListeners = new LinkedList<JukeboxClientListener>();

    public void addJukeboxClientListener(JukeboxClientListener listener){
        clientListeners.add(listener);
    }

    public boolean removeJukeboxClientListener(JukeboxClientListener listener){
        return clientListeners.remove(listener);
    }

    public JukeboxClient (String host, int port, String login, String password) {
        m_Host = host;
        m_Port = port;
        m_Login = login;
        m_Password = password;

        httpClient = new DefaultHttpClient();
        Credentials defaultCredentials = new UsernamePasswordCredentials(m_Login, m_Password);
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(m_Host, m_Port, AuthScope.ANY_REALM), defaultCredentials);
    }



    private class RequestTask extends AsyncTask<String, Void, String> {


        private Exception exception;

        protected String doInBackground(String... queries) {
            this.exception = null;
            try {
                String query=queries[0];
                String result = "";

                HttpPost httpPost = new HttpPost("http://" + m_Host + ":" + Integer.toString(m_Port) + "/" + s_JsonApiPath);

                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                httpPost.setEntity(new StringEntity(query, HTTP.UTF_8));

                System.out.println(httpPost.getEntity().toString());
                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                System.out.println("Login form get: " + response.getStatusLine());
                if (entity != null) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        result += inputLine;
                    }
                    in.close();
                }
                return result;
            } catch (IOException e) {
                this.exception = e;
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }


        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    System.out.println(result);
                    JSONObject response = new JSONObject(result);
                    m_Timestamp = response.getInt("timestamp");
                    m_Channel = ParseChannel(response);

                    for(JukeboxClientListener t: clientListeners){
                        t.requestSuccess();
                    }
                }
            } catch (JSONException e) {
                this.exception = e;
            }
            if (this.exception != null) {
                this.exception.printStackTrace();
                for(JukeboxClientListener t: clientListeners){
                    t.requestFailure(this.exception);
                }
            }
        }
    }

    public void Next () {
        try {
            new RequestTask().execute(BuildRequest(true).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void Reload () {
        try {
            new RequestTask().execute(BuildRequest(false).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject BuildRequest(boolean next) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("timestamp", m_Timestamp);
        if (next == true) {
            JSONObject action = new JSONObject();
            action.put("name", "next");
            result.put("action", action);
        }
        return result;
    }

    private Channel ParseChannel(JSONObject root) {
        Channel channel = new Channel();

        JSONObject channel_infos = null;
        try {
            channel_infos = root.getJSONObject("channel_infos");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        try {
            channel.listener_count = channel_infos.getInt("listener_count");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        try {
            channel.elapsed = channel_infos.getInt("elapsed");
        } catch (JSONException e) {
            //e.printStackTrace();
        }

        try {
            channel.current_song = ParseSong(root.getJSONObject("current_song"));
        } catch (JSONException e) {
            //e.printStackTrace();
        }

        channel.play_queue = new LinkedList<Song>();
        try {
            JSONObject play_queue_json = root.getJSONObject("play_queue");
            JSONArray songs_json = play_queue_json.getJSONArray("songs");
            for (int i = 0; i < songs_json.length(); i++)
            {
                try {
                    Song song = ParseSong(songs_json.getJSONObject(i));
                    channel.play_queue.add(song);
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return channel;
    }

    private Song ParseSong(JSONObject jsonSong) {
        Song song = new Song();
        try {
            song.mid = jsonSong.getInt("mid");
            song.title = jsonSong.getString("title");
            song.artist = jsonSong.getString("artist");
            song.album = jsonSong.getString("album");
            song.duration = jsonSong.getInt("duration");
            song.genre = jsonSong.getInt("genre");
            song.year = jsonSong.getInt("years");
            song.track = jsonSong.getInt("track");
            song.trackNb = jsonSong.getInt("trackNb");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return song;
    }
}
