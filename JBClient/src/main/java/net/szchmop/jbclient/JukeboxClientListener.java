package net.szchmop.jbclient;

/**
 * Created by beuiot on 23/09/13.
 */
public interface JukeboxClientListener {
    void requestSuccess ();
    void requestFailure (Exception e);
}
