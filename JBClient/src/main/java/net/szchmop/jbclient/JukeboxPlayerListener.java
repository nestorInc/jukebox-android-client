package net.szchmop.jbclient;

/**
 * Created by upperbyte on 25/09/13.
 */
public interface JukeboxPlayerListener {
    public enum PlayerState {
        STOPPED,
        CONNECTING,
        BUFFERING,
        PLAYING,
        PAUSED,
        ERROR
    }

    void onPlayerStatusChanged (PlayerState state);
    void playerBusy (Boolean busy);
}
