package net.szchmop.jbclient;

import android.media.MediaPlayer;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.LinkedList;

/**
 * Created by upperbyte on 24/09/13.
 */
public class JukeboxPlayer implements MediaPlayer.OnPreparedListener {

    private String m_Host;
    private int m_Port;
    private String m_Login;
    private String m_Password;

    //LISTENER STUFF
    private LinkedList<JukeboxPlayerListener> clientListeners = new LinkedList<JukeboxPlayerListener>();

    public void addJukeboxClientListener(JukeboxPlayerListener listener){
        clientListeners.add(listener);
    }

    public boolean removeJukeboxClientListener(JukeboxPlayerListener listener){
        return clientListeners.remove(listener);
    }


    public MediaPlayer m_MediaPlayer;


    private JukeboxPlayerListener.PlayerState m_State;
    public JukeboxPlayerListener.PlayerState getState () {
        return m_State;
    }
    private void setState (JukeboxPlayerListener.PlayerState state) {
        m_State = state;
        for(JukeboxPlayerListener l: clientListeners){
            l.onPlayerStatusChanged(m_State);
        }
    }

    public JukeboxPlayer (String host, int port, String login, String password) {
        m_Host = host;
        m_Port = port;
        m_Login = login;
        m_Password = password;

        m_State = JukeboxPlayerListener.PlayerState.STOPPED;
        m_MediaPlayer = new MediaPlayer();
        m_MediaPlayer.setOnPreparedListener(this);
    }

    public void Play () {
        this.setState(JukeboxPlayerListener.PlayerState.CONNECTING);
        try {
            m_MediaPlayer.setDataSource("http://" + m_Login + ":" + m_Password + "@" + m_Host + ":" + m_Port + "/stream");
            m_MediaPlayer.prepareAsync();

        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void Stop () {
        if (m_MediaPlayer.isPlaying()) {
            m_MediaPlayer.stop();
            m_MediaPlayer.reset();
            this.setState(JukeboxPlayerListener.PlayerState.STOPPED);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.setState(JukeboxPlayerListener.PlayerState.PLAYING);
        m_MediaPlayer.start();
    }
}
