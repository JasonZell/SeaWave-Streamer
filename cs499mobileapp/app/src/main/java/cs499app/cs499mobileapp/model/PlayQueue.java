package cs499app.cs499mobileapp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by centa on 7/12/2017.
 */

public class PlayQueue {

    private int lastPlayedPlaylistViewID;
    private List<StationRecord> stationQueue;
    private List<Integer> shuffleQueue;
    private List<Integer> historyQueue;
    private boolean shuffleOn;
    private int currentStationViewID;

    public PlayQueue(List<StationRecord> record, boolean shuffleState) {
        stationQueue = record;
        shuffleOn = shuffleState;
        lastPlayedPlaylistViewID = -1;
        currentStationViewID = -1;
        shuffleQueue = new ArrayList<>();
        historyQueue = new ArrayList<>();
    }


    // set it on will create shuffle list.
    // set it off will reset shuffle list
    public void setShuffleOn(boolean shuffleOn) {
        this.shuffleOn = shuffleOn;

        if(shuffleOn == true)
        {
            initShuffleQueue();
        }
        else
        {
            shuffleQueue.clear();
        }
    }

    public void initShuffleQueue()
    {
        for(int i = 0; i < stationQueue.size();++i)
        {
            shuffleQueue.add(i);
        }
        Collections.shuffle(shuffleQueue);
    }

    public int getCurrentStationViewID() {
        return currentStationViewID;
    }

    public void setCurrentStationViewID(int currentStationViewID) {
        this.currentStationViewID = currentStationViewID;
    }

    public List<StationRecord> getStationQueue() {
        return stationQueue;
    }

    public void setStationQueue(List<StationRecord> stationQueue) {
        this.stationQueue = stationQueue;
    }

    public void resetShuffleQueue()
    {
        shuffleQueue.clear();
    }

    public void resetPlayQueue()
    {
        historyQueue.clear();
        resetShuffleQueue();
    }


    // For when the new song is added, this will increment number of songs in list by 1.
    // reshuffle the shuffle list
    public void incrementIndexEntry()
    {
        int newSize = stationQueue.size();
        shuffleQueue.add(newSize);
    }

    // For when a song is removed, everything will reset and re-initialized.
    // if the song currently playing is not the one being removed.
    // add that current song index to the newly created history arraylist.
    public void removeIndex(int i)
    {
        resetPlayQueue();
        if(currentStationViewID != i)
        {
            historyQueue.add(i);
        }

        if(shuffleOn == true)
        {
            initShuffleQueue();
        }
    }

    public void setPlayListViewID(int viewID)
    {

        lastPlayedPlaylistViewID = viewID;
    }

    public int getNextStation()
    {
        return 0;
    }

    public int getPrevStation()
    {
        return 0;
    }

    public boolean isSamePlaylist(int playlistViewID)
    {
        boolean result = false;
        if(playlistViewID != lastPlayedPlaylistViewID)
        {
            result = false;
        }
        else
        {
            result = true;
        }
         return result;
    }
}
