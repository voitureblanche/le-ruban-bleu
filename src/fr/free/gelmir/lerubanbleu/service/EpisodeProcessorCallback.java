package fr.free.gelmir.lerubanbleu.service;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 26/03/13
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public interface EpisodeProcessorCallback {

    public enum Result {
        OK,
        KO
    }

    void send(Result result, Episode episode);

}
